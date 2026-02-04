export interface Reunion {
    id_reunion: number;
    estado: EstadoReunion;
    estado_eus?: EstadoReunionEus | null;
    profesor_id?: number | null;
    alumno_id?: number | null;
    id_centro?: string | null;
    titulo?: string | null;
    asunto?: string | null;
    aula?: string | null;
    fecha?: string | null; // ISO datetime
    created_at: string;
    updated_at: string;
}

export enum EstadoReunion {
  PENDIENTE = 'pendiente',
  ACEPTADA = 'aceptada',
  DENEGADA = 'denegada',
  CONFLICTO = 'conflicto'
}

export enum EstadoReunionEus {
  ONARTZEKE = 'onartzeke',
  ONARTUTA = 'onartuta',
  EZEZTATUTA = 'ezeztatuta',
  GATAZKA = 'gatazka'
}