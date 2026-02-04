import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../../interface/user';
import { Reunion } from '../../interface/reunion';
import { Horario } from '../../interface/horario';
import { Centro } from '../../interface/centro';

@Injectable({
  providedIn: 'root',
})
export class Consultas {

  private apiUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  getTotalProfesores() {
    //http://localhost:3000/profesores/count
    return this.http.get<{ total: number }>(`${this.apiUrl}/profesores/count`);
  }

  getTotalAlumnos() {
    //http://localhost:3000/alumnos/count
    return this.http.get<{ total: number }>(`${this.apiUrl}}/alumnos/count`);
  }

  getTotalReunionesHoy(){
    //http://localhost:3000/reuniones/count/today
    return this.http.get<{ total: number }>(`${this.apiUrl}/reuniones/count/today`);
  }

  getUserById(id: number) {
    //http://localhost:3000/users/${id}
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  getListadoProfesores() {
    //http://localhost:3000/profesores
    return this.http.get<User[]>(`${this.apiUrl}/profesores`);
  }

  getListadoAlumnos() {
    //http://localhost:3000/alumnos
    return this.http.get<User[]>(`${this.apiUrl}/alumnos`);
  }

  getListadoAdministradores() {
    //http://localhost:3000/administradores
    return this.http.get<User[]>(`${this.apiUrl}/administradores`);
  }

  updateUser(id: number, updatedData: Partial<User>) {
    //http://localhost:3000/users/${id}
    return this.http.put<{ message: string }>(`${this.apiUrl}/users/${id}`, updatedData);
  }

  createUser(userData: Partial<User>) {
    //http://localhost:3000/users
    return this.http.post<{ message: string, id: number }>(`${this.apiUrl}/users`, userData);
  }

  deleteUser(id: number) {
    //http://localhost:3000/users/${id}
    return this.http.delete<{ message: string}>(`${this.apiUrl}/users/${id}`);
  }

  getReunionesProfesor(id: number) {
    //http://localhost:3000/profesores/${id}/reuniones
    return this.http.get<Reunion[]>(`${this.apiUrl}/profesores/${id}/reuniones`);
  }

  getReunionesAlumno(id: number) {
    //http://localhost:3000/alumnos/${id}/reuniones
    return this.http.get<Reunion[]>(`${this.apiUrl}/alumnos/${id}/reuniones`);
  }

  getReuniones(){
    //http://localhost:3000/reuniones
    return this.http.get<Reunion[]>(`${this.apiUrl}/reuniones`);
  }

  getHorarioProfesor(id: number) {
    //http://localhost:3000/profesores/${id}/horarios
    return this.http.get<Horario[]>(`${this.apiUrl}/profesores/${id}/horarios`);
  }

  getHorarioAlumno(id: number) {
    //http://localhost:3000/alumnos/${id}/horarios
    return this.http.get<Horario[]>(`${this.apiUrl}/alumnos/${id}/horarios`);
  }

  createReunion(reunionData: Partial<Reunion>) {
    //http://localhost:3000/reuniones
    return this.http.post<{ message: string, id: number }>(`${this.apiUrl}/reuniones`, reunionData);
  }

  getCentros() {
    return this.http.get<{ CENTROS: Centro[] }>('/EuskadiLatLon.json');
  }
}