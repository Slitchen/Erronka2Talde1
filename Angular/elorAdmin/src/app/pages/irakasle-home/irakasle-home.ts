import { Component, computed, signal } from '@angular/core';
import { Consultas } from '../../services/consultas/consultas';
import { Reunion } from '../../interface/reunion';
import { Horario } from '../../interface/horario';
import { User } from '../../interface/user';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { Centro } from '../../interface/centro';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-irakasle-home',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, TranslateModule],
  templateUrl: './irakasle-home.html',
  styleUrl: './irakasle-home.css',
})

export class IrakasleHome {
  // DATOS DEL ALUMNO 
  id = signal(0);
  email = signal('');
  username = signal('');
  password = signal('');
  nombre = signal('');
  apellidos = signal('');
  dni = signal('');
  direccion = signal('');
  telefono1 = signal('');
  telefono2 = signal('');
  tipo_id = signal(0);
  argazkia_url = signal('');

  // LISTAS ORIGINALES
  listadoProfesores = signal<User[]>([]);
  listadoAlumnos = signal<User[]>([]);
  centros = signal<Centro[]>([]);
  reuniones = signal<Reunion[]>([]);
  horarios = signal<Horario[]>([]);

  // LISTAS FILTRADAS
  listadoProfesoresFiltrados = signal<User[]>([]);
  listadoAlumnosFiltrados = signal<User[]>([]);
  
  // ARRAY DE LOS DIAS DE LA SEMANA
  diasSemana: string[] = ['DIAS.LUNES', 'DIAS.MARTES', 'DIAS.MIERCOLES', 'DIAS.JUEVES', 'DIAS.VIERNES'];

  // FORMULARIO
  createForm: FormGroup;

  /* MOSTRAR FORMULARIO */
  showCreateForm = signal(false);

  // FILTROS
  filtroDTituc = signal<string>(''); // FILTRO DE TITULARIDAD
  filtroDTerre = signal<string>(''); // FILTRO DE TERRITORIO
  filtroDMunic = signal<string>(''); // FILTRO DE MUNICIPIO

  // PAGINACION
  paginaActual = signal(1); // PAGINA ACTUAL
  itemsPorPagina = 10; // CENTROS POR PAGINA

  // MOSTRAR DETALLES
  showDetails = signal<number[]>([]);

  // FILTRO DE BUSQUEDA DE USUARIO
  filtroBusqueda = new FormControl('');

  // CONSTRUCTOR
  constructor(
    private fb: FormBuilder,
    private consultasService: Consultas) {
    
    // FILTRO PARA BUSCAR USUARIO
    this.filtroBusqueda.valueChanges.subscribe(filtro => {
      const valor = filtro?.toLowerCase() || '';

      // FILTRAR LA LISTA DE TODOS LOS PROFESORES MENOS EL ACTUAL POR EL FILTRO INTRODUCIDO EN EL FILTRO DE BUSQUEDA
      this.listadoProfesoresFiltrados.set(
        this.listadoProfesoresFiltrado()
          .filter(p => {
            const nombreCompleto = `${p.nombre ?? ''} ${p.apellidos ?? ''}`.toLowerCase();
            return nombreCompleto.includes(valor) || (p.tipo_id?.toString().includes(valor) ?? false);
          })
      );

      // FILTRAR LA LISTA DE TODOS LOS ALUMNOS POR EL FILTRO INTRODUCIDO EN EL FILTRO DE BUSQUEDA
      this.listadoAlumnosFiltrados.set(
        this.listadoAlumnos()
          .filter(a => {
            const nombreCompleto = `${a.nombre ?? ''} ${a.apellidos ?? ''}`.toLowerCase();
            return nombreCompleto.includes(valor) || (a.tipo_id?.toString().includes(valor) ?? false);
          })
      );
    });
    
    // FORMULARIO CREAR REUNION + VALIDACIONES
    this.createForm = this.fb.group({
      nuevoEstado: ['pendiente'],
      nuevoProfesor_id: [this.id()],
      nuevoAlumno_id: ['', Validators.required], // EL ID DEL ALUMNO SE CONSIGUE CON EL VALOR DE LA OPCION SELECCIONADA EN EL DESPLEGABLE DEL FORMULARIO
      nuevoCentro: ['', Validators.required], // EL ID DEL CENTRO SE CONSIGUE CON EL VALOR DE LA OPCION SELECCIONADA EN EL DESPLEGABLE DEL FORMULARIO
      nuevoTitulo: ['', Validators.required],
      nuevoAsunto: ['', Validators.required],
      nuevaAula: [''],
      nuevaFecha: ['', Validators.required]
    });
  }

  /* METODO QUE SE EJECUTA AL INICIALIZAR LA PAGINA */
  ngOnInit(): void {
    console.log(this.argazkia_url());
    this.cargarDatos();
  }

  /* METODO PARA CARGAR LOS DATOS DE LA PAGINA */
  cargarDatos() {
    // CARGAR EL USUARIO GUARDADO EN SESSIONSTORAGE
    const user = JSON.parse(sessionStorage.getItem('user')!);
    // CARGAR EL ID DEL USUARIO
    const id = user.id;

    // CARGAR LOS DATOS DEL USUARIO QUE HA INICIADO SESION
    this.consultasService.getUserById(id).subscribe(data => {
      this.createForm.patchValue({
        nuevoProfesor_id: data.id
      });
      this.id.set(data.id);
      this.email.set(data.email);
      this.username.set(data.username);
      this.password.set(data.password);
      this.nombre.set(data.nombre ?? '');
      this.apellidos.set(data.apellidos ?? '');
      this.dni.set(data.dni ?? '');
      this.direccion.set(data.direccion ?? '');
      this.telefono1.set(data.telefono1 ?? '');
      this.telefono2.set(data.telefono2 ?? '');
      this.tipo_id.set(data.tipo_id);
      this.argazkia_url.set(data.argazkia_url ?? '');
    });

    // CARGAR LAS REUNIONES
    this.consultasService.getReunionesProfesor(id).subscribe(data => {
      this.reuniones.set(data);
    });

    // CARGAR EL HORARIO
    this.consultasService.getHorarioProfesor(id).subscribe(data => {
      this.horarios.set(data);
    });

    // CARGAR LAS LISTAS DE PROFESORES
    this.consultasService.getListadoProfesores().subscribe(data => {
        this.listadoProfesores.set(data);
        this.listadoProfesoresFiltrados.set(data);
    });

    // CARGAR LA LISTA DE ALUMNOS
    this.consultasService.getListadoAlumnos().subscribe(data => {
      this.listadoAlumnos.set(data);
      this.listadoAlumnosFiltrados.set(data);
    });

    // CARGAR LA LISTA DE CENTROS
    this.consultasService.getCentros().subscribe(data => {
      this.centros.set(data.CENTROS);
    });
  }

  // FILTRAR LA LISTA DE PROFESORES PARA OBTENER TODOS LOS PROFESORES MENOS CON EL QUE HE INICIADO SESION
  listadoProfesoresFiltrado = computed(() => {
    const idActual = this.id();
    return this.listadoProfesores().filter(prof => prof.id !== idActual);
  });

  // CREAR HORARIO SEMANAL Y CARGAR DATOS EN EL
  horarioSemanal = computed(() => {
    const dias = this.diasSemana;
    const horas = ['1', '2', '3', '4', '5', '6'];

    const mapDias: Record<string, string> = {
      lunes: 'DIAS.LUNES',
      martes: 'DIAS.MARTES',
      miercoles: 'DIAS.MIERCOLES',
      miércoles: 'DIAS.MIERCOLES',
      jueves: 'DIAS.JUEVES',
      viernes: 'DIAS.VIERNES'
    };

    const matriz: { [hora: string]: { [dia: string]: string } } = {};

    for (const hora of horas) {
      matriz[hora] = {};
      for (const dia of dias) {
        matriz[hora][dia] = '';
      }
    }

    for (const h of this.horarios()) {
      const diaKey = mapDias[h.dia.toLowerCase()];
      const hora = h.hora.toString().split('.')[0];

      if (diaKey && matriz[hora]) {
        matriz[hora][diaKey] = h.modulo_nombre;
      }
    }

    return matriz;
  });


  // METODO PARA MOSTRAR DETALLES DEL USUARIO
  mostrarDetalles(idUser: number) {
    this.showDetails.update(arr =>
      arr.includes(idUser)
        ? arr.filter(i => i !== idUser)
        : [...arr, idUser]
    );
  }

  // METODO PARA MOSTRAR EL FORMULARO DE CREAR REUNION
  mostrarCrearReunion() {
    this.showCreateForm.set(true);
  }

  // METODO PARA CREAR LA REUNION EN LA BASE DE DATOS
  crearReunion() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    const formValue = this.createForm.value;

    const nuevaReunion: Partial<Reunion> = {
      estado: formValue.nuevoEstado,
      profesor_id: Number(formValue.nuevoProfesor_id),
      alumno_id: Number(formValue.nuevoAlumno_id),
      id_centro: formValue.nuevoCentro,
      titulo: formValue.nuevoTitulo,
      asunto: formValue.nuevoAsunto,
      aula: formValue.nuevaAula,
      fecha: formValue.nuevaFecha
    };


    console.log('Enviando reunión:', nuevaReunion);

    this.consultasService.createReunion(nuevaReunion).subscribe({
      next: res => {
        alert(res.message);
        this.showCreateForm.set(false);
        this.createForm.reset();
        this.cargarDatos();
      },
      error: err => console.error('Error creando reunion:', err)
    });
  }

  // METODO PARA CERRAR FORMULARIO
  cancelarCrear() {
    this.showCreateForm.set(false);
  }

  // FILTRO DE TITULARIDAD
  titularidades = computed(() => {
    const centros = this.centros();
    return [...new Set(centros.map(c => c.DTITUC))].sort();
  });

  // FILTRO DE TERRITORIO CONDICIONADO POR TITULARIDAD
  territorios = computed(() => {
    if (!this.filtroDTituc()) return [];
    return [...new Set(
      this.centros()
        .filter(c => c.DTITUC === this.filtroDTituc())
        .map(c => c.DTERRE)
    )].sort();
  });

  // FILTRO DE MUNICIPIO CONDICIONADO POR TITULARIDAD Y TERRITORIO
  municipios = computed(() => {
    if (!this.filtroDTituc() || !this.filtroDTerre()) return [];
    return [...new Set(
      this.centros()
        .filter(c => c.DTITUC === this.filtroDTituc() && c.DTERRE === this.filtroDTerre())
        .map(c => c.DMUNIC)
    )].sort();
  });

  // METODO PARA VOLVEL A LA PRIMERA PAGINA CADA VEZ QUE UTILIZO UN FILTRO
  resetPagina() {
    this.paginaActual.set(1);
  }

  // FILTRAR LOS CENTROS POR TITULARIDAD, TERRITORIO Y MUNICIPIO
  centrosFiltrados = computed(() => {
    return this.centros().filter(c =>
      (!this.filtroDTituc() || c.DTITUC === this.filtroDTituc()) &&
      (!this.filtroDTerre() || c.DTERRE === this.filtroDTerre()) &&
      (!this.filtroDMunic() || c.DMUNIC === this.filtroDMunic())
    );
  });

  // CANTIDAD DE CENTROS FILTRADOS
  totalFiltrados = computed(() => this.centrosFiltrados().length);

  // CENTRO QUE APARECEN POR PAGINA
  centrosPaginados = computed(() => {
    const inicio = (this.paginaActual() - 1) * this.itemsPorPagina;
    return this.centrosFiltrados().slice(inicio, inicio + this.itemsPorPagina);
  });

  // CONSEGUIR TOTAL DE PAGINAS CREADAS CON LA PAGINACION
  totalPaginas = computed(() => {
    return Math.ceil(this.centrosFiltrados().length / this.itemsPorPagina);
  });

  // METODO PARA IR A LA PRIMERA PAGINA DE LA PAGINACION
  irPrimeraPagina() {
    this.paginaActual.set(1);
  }

  // METODO PARA IR A LA ULTIMA PAGINA DE LA PAGINACION
  irUltimaPagina() {
    this.paginaActual.set(this.totalPaginas());
  }

  // METODO PARA IR A LA PAGINA ANTERIOR A LA ACTUAL DE LA PAGINACION
  paginaAnterior() {
    if (this.paginaActual() > 1) {
      this.paginaActual.update(p => p - 1);
    }
  }

  // METODO PARA IR A LA SIGUIENTE PAGINA A LA ACTUAL DE LA PAGINACION
  paginaSiguiente() {
    if (this.paginaActual() < this.totalPaginas()) {
      this.paginaActual.update(p => p + 1);
    }
  }
}
