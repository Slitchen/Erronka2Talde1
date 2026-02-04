import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { User } from '../../interface/user';


@Injectable({ providedIn: 'root' })
export class Auth {

  //URL DE LA API
  private apiUrl = 'http://localhost:3000';
  //ESTADO DEL USUARIO AUTENTICADO
  private userSubject = new BehaviorSubject<User | null>(null);

  constructor(private http: HttpClient) {
    //CARGAR USUARIO DESDE SESSIONSTORAGE SI EXISTE
    const saved = sessionStorage.getItem('user');
    if (saved) {
      this.userSubject.next(JSON.parse(saved));
    }
  }

  //MÉTODO DE LOGIN. DEVUELVE UN OBSERVABLE DEL USUARIO AUTENTICADO
  login(username: string, password: string): Observable<User> {
    return new Observable<User>(observer => {
      this.http.post<User>(`${this.apiUrl}/login`, { username, password }).subscribe({
        next: (user) => {
          this.setUser(user);
          observer.next(user);
          observer.complete();
        },
        error: (err) => observer.error(err)
      });
    });
  }

  //GUARDAR USUARIO EN SESSIONSTORAGE Y ACTUALIZAR EL ESTADO
  setUser(user: User) {
    sessionStorage.setItem('user', JSON.stringify(user));
    this.userSubject.next(user);
  }

  //MÉTODO DE LOGOUT. ELIMINA EL USUARIO DE SESSIONSTORAGE Y ACTUALIZA EL ESTADO
  logout() {
    sessionStorage.removeItem('user');
    this.userSubject.next(null);
  }

  //OBSERVABLE PARA ESCUCHAR CAMBIOS EN EL ESTADO DEL USUARIO
  onUserChange() {
    return this.userSubject.asObservable();
  }

  //OBTENER EL TEXTO DEL ROL SEGÚN EL TIPO DE USUARIO
  getRoleText(tipo_id?: number): string {
    switch (tipo_id) {
      case 1: return 'GOD';
      case 2: return 'ADMINISTRADOR';
      case 3: return 'PROFESOR';
      case 4: return 'ALUMNO';
      default: return '';
    }
  }
}