export interface User {
    id: number;
    email: string;
    username: string;
    password: string;
    nombre?: string | null;
    apellidos?: string | null;
    dni?: string | null;
    direccion?: string | null;
    telefono1?: string | null;
    telefono2?: string | null;
    tipo_id: number;
    argazkia_url?: string | null;
    created_at: string;
    updated_at: string;
}
