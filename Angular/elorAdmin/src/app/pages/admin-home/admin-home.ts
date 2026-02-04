import { Component, computed, signal } from '@angular/core';
import { Consultas } from '../../services/consultas/consultas';
import { User } from '../../interface/user';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { RouterLink } from '@angular/router';
import { Reunion } from '../../interface/reunion';

@Component({
  selector: 'app-admin-home',
  imports: [ReactiveFormsModule, CommonModule, TranslateModule, RouterLink],
  templateUrl: './admin-home.html',
  styleUrl: './admin-home.css',
})

export class AdminHome {
  /* ENCABEZADO */
  totalProfesores = signal(0)
  totalAlumnos = signal(0)
  totalReunionesHoy = signal(0);
  argazkia_url = signal('');

  /* LISTAS ORIGINALES */
  listadoProfesores = signal<User[]>([]);
  listadoAlumnos = signal<User[]>([]);
  listadoAdministradores = signal<User[]>([]);
  reuniones = signal<Reunion[]>([]);

  /* LISTAS FILTRADAS */
  listadoProfesoresFiltrados = signal<User[]>([]);
  listadoAlumnosFiltrados = signal<User[]>([]);
  listadoAdministradoresFiltrados = signal<User[]>([]);

  // PAGINACION
  paginaActual = signal(1); // PAGINA ACTUAL
  itemsPorPagina = 6; // CENTROS POR PAGINA

  /* FORMULARIOS */
  updateForm: FormGroup;
  createForm: FormGroup;

  /* MOSTRAR FORMULARIO */
  showCreateForm = signal(false);
  
  /* VALORES PARA FORMULARIO UPDATE */
  editId = signal<number | null>(null);

  /* FILTRO PARA BUSQUEDA DE USUARIO */
  filtroBusqueda = new FormControl('');
  
  /* CONSTRUCTOR */
  constructor(
    private fb: FormBuilder,
    private consultasService: Consultas
  ) {

    /* FORMULARIO UPDATE + VALIDACIONES*/
    this.updateForm = this.fb.group({
      nombre: [''],
      apellidos: [''],
      email: ['', Validators.email],
      telefono1: ['', Validators.pattern('^[0-9]{9}$')],
      telefono2: ['', Validators.pattern('^[0-9]{9}$')],
      direccion: [''],
      argazkia_url: ['']
    });

    /* FORMULARIO CREATE + VALIDACIONES + VALORES POR DEFECTO */
    this.createForm = this.fb.group({
      nuevoNombre: ['', Validators.required],
      nuevoApellidos: ['', Validators.required],
      nuevoEmail: ['', [Validators.required, Validators.email]],
      nuevoUsername: ['', Validators.required],
      nuevoPassword: ['', Validators.required],
      nuevoTelefono1: ['', Validators.pattern('^[0-9]{9}$')],
      nuevoTelefono2: ['', Validators.pattern('^[0-9]{9}$')],
      nuevaDireccion: [''],
      nuevoTipoId: [3, Validators.required],
      nuevoArgazkia_url: ['../ezezaguna.jpg']
    });

    /* FILTRAR LISTAS PARA FILTRO DE BUSQUEDA DE USUARIO */
    this.filtroBusqueda.valueChanges.subscribe(filtro => {
      const valor = filtro?.toLowerCase() || '';

      /* FILTRAR LISTA DE PROFESORES */
      this.listadoProfesoresFiltrados.set(
        this.listadoProfesores()
          .filter(p => {
            const nombreCompleto = `${p.nombre ?? ''} ${p.apellidos ?? ''}`.toLowerCase();
            return nombreCompleto.includes(valor) || (p.tipo_id?.toString().includes(valor) ?? false);
          })
      );

      /* FILTRAR LISTA DE ALUMNOS */
      this.listadoAlumnosFiltrados.set(
        this.listadoAlumnos()
          .filter(a => {
            const nombreCompleto = `${a.nombre ?? ''} ${a.apellidos ?? ''}`.toLowerCase();
            return nombreCompleto.includes(valor) || (a.tipo_id?.toString().includes(valor) ?? false);
          })
      );

      /* FILTRAR LISTA DE ADMINISTRADORES */
      this.listadoAdministradoresFiltrados.set(
        this.listadoAdministradores()
          .filter(adm => {
            const nombreCompleto = `${adm.nombre ?? ''} ${adm.apellidos ?? ''}`.toLowerCase();
            return nombreCompleto.includes(valor) || (adm.tipo_id?.toString().includes(valor) ?? false);
          })
      );
    });
  }

  /* METODO QUE SE EJECUTA AL INICIALIZAR LA PAGINA */
  ngOnInit(): void {
      this.cargarDatos();
  }

  /* METODO PARA CARGAR LOS DATOS DE LA PAGINA */
  cargarDatos(){
    /* CARGAR EL TOTAL DE PROFESORES */
      this.consultasService.getTotalProfesores().subscribe(data => {
        this.totalProfesores.set(data.total);
      });

      /* CARGAR EL TOTAL DE ALUMNOS */
      this.consultasService.getTotalAlumnos().subscribe(data => {
        this.totalAlumnos.set(data.total);
      });

      /* CARGAR EL TOTAL DE REUNIONES PARA HOY */
      this.consultasService.getTotalReunionesHoy().subscribe(data => {
        this.totalReunionesHoy.set(data.total);
      });

      /* CARGAR LAS REUNIONES */
      this.consultasService.getReuniones().subscribe(data => {
        this.reuniones.set(data);
      });

      /* CARGAR LA LISTA DE LOS PROFESORES */
      this.consultasService.getListadoProfesores().subscribe(data => {
        this.listadoProfesores.set(data);
        this.listadoProfesoresFiltrados.set(data);
      });

      /* CARGAR LA LISTA DE LOS ALUMNOS */
      this.consultasService.getListadoAlumnos().subscribe(data => {
        this.listadoAlumnos.set(data);
        this.listadoAlumnosFiltrados.set(data);
      });
  }

  /* FORMULARIO UPDATE */
  /* METODO PARA SETEAR LOS DATOS DEL FORMULARIO UPDATE EN EL USUARIO */
  updateUsuario(user: User) {
    this.editId.set(user.id);

    this.updateForm.patchValue({
      nombre: user.nombre ?? '',
      apellidos: user.apellidos ?? '',
      email: user.email ?? '',
      telefono1: user.telefono1 ?? '',
      telefono2: user.telefono2 ?? '',
      direccion: user.direccion ?? '',
      argazkia_url: user.argazkia_url ?? ''
    });
  }

  /* METODO PARA ENVIAR LOS DATOS DEL FORMULARIO UPDATE A LA BASE DE DATOS */
  guardarUpdate() {
    if (this.updateForm.invalid) {
      this.updateForm.markAllAsTouched();
      return;
    }

    if (this.editId() === null) return;

    const updatedData: Partial<User> = this.updateForm.value;

    this.consultasService.updateUser(this.editId()!, updatedData).subscribe({
      next: res => {
        alert(res.message);
        this.editId.set(null);        // cerrar formulario
        this.updateForm.reset();       // limpiar formulario
        this.cargarDatos();           // recargar listas
      },
      error: err => console.error(err)
    });
  }

  /* METODO PARA CERRAR EL FORMULARIO UPDATE Y RESETEAR LOS CAMPOS*/
  cancelarUpdate() {
    this.editId.set(null);        // cerrar formulario update
    this.updateForm.reset();      // limpiar campos
  }

  /* FORMULARIO CREATE */
  /* METODO PARA MOSTRAR EL FORMULARIO CREATE */
  mostrarCrearUsuario() {
    this.showCreateForm.set(true);
  }

  /* METODO PARA SETEAR LOS DATOS DEL FORMULARIO CREATE EN EL USUARIO Y MANDARLO A LA BASE DE DATOS */
  crearUsuario() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    const formValue = this.createForm.value;

    const nuevoUsuario: Partial<User> = {
      nombre: formValue.nuevoNombre,
      apellidos: formValue.nuevoApellidos,
      email: formValue.nuevoEmail,
      username: formValue.nuevoUsername,
      password: formValue.nuevoPassword,
      telefono1: formValue.nuevoTelefono1,
      telefono2: formValue.nuevoTelefono2,
      direccion: formValue.nuevaDireccion,
      tipo_id: formValue.nuevoTipoId,
      argazkia_url: formValue.nuevoUrlArgazkia
    };

    this.consultasService.createUser(nuevoUsuario).subscribe({
      next: res => {
        alert(res.message);
        this.showCreateForm.set(false);
        this.createForm.reset({ nuevoTipoId: 3 });
        this.cargarDatos();
      },
      error: err => console.error('Error creando usuario:', err)
    });
  }

  /* METODO PARA CERRAR EL FORMULARIO CREAR Y RESETEAR LOS CAMPOS */
  cancelarCrear() {
    this.showCreateForm.set(false);     // ocultar formulario crear
    this.createForm.reset({ nuevoTipoId: 3 }); // reset + valor por defecto
  }

  /* METODO PARA ELIMINAR USUARIO DE LA BASE DE DATOS */
  borrarUsuario(user: User) {
    if (!confirm('¿Seguro que deseas borrar este usuario?')) return;

    console.log('Borrando usuario con ID:', user.id, 'tipo:', typeof user.id);

    this.consultasService.deleteUser(user.id).subscribe({
      next: res => {
        alert(res.message);
        this.cargarDatos();
      },
      error: err => console.error(err)
    });
  }

  // REUNIONES QUE APARECEN POR PAGINA
  reunionesPaginadas = computed(() => {
    const inicio = (this.paginaActual() - 1) * this.itemsPorPagina;
    return this.reuniones().slice(inicio, inicio + this.itemsPorPagina);
  });

  // CONSEGUIR TOTAL DE PAGINAS CREADAS CON LA PAGINACION
  totalPaginas = computed(() => {
    return Math.ceil(this.reuniones().length / this.itemsPorPagina);
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