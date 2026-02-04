export interface Horario {
    id: number;
    dia: DiaSemana;
    hora: number;
    profe_id: number;
    modulo_id: number;
    aula?: string | null;
    observaciones?: string | null;
    created_at: string;
    updated_at: string;
}

export enum DiaSemana {
  LUNES = 'LUNES',
  MARTES = 'MARTES',
  MIERCOLES = 'MIERCOLES',
  JUEVES = 'JUEVES',
  VIERNES = 'VIERNES'
}