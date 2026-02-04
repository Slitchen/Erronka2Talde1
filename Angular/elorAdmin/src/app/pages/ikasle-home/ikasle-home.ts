import { Component, signal, computed } from '@angular/core';
import { Consultas } from '../../services/consultas/consultas';
import { Reunion } from '../../interface/reunion';
import { Horario } from '../../interface/horario';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-ikasle-home',
  imports: [CommonModule, RouterLink, TranslateModule],
  templateUrl: './ikasle-home.html',
  styleUrl: './ikasle-home.css',
})

export class IkasleHome {
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

  // LISTAS
  reuniones = signal<Reunion[]>([]);
  horarios = signal<Horario[]>([]);

  // ARRAY DE LOS DIAS DE LA SEMANA
  diasSemana: string[] = ['DIAS.LUNES', 'DIAS.MARTES', 'DIAS.MIERCOLES', 'DIAS.JUEVES', 'DIAS.VIERNES'];

  // CONSTRUCTOR
  constructor(private consultasService: Consultas) {}

  /* METODO QUE SE EJECUTA AL INICIALIZAR LA PAGINA */
  ngOnInit(): void {
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
    this.consultasService.getReunionesAlumno(id).subscribe(data => {
      this.reuniones.set(data);
    });

    // CARGAR EL HORARIO
    this.consultasService.getHorarioAlumno(id).subscribe(data => {
      this.horarios.set(data);
    });
  }

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
}
