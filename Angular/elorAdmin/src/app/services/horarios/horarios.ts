import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Horario } from '../../interface/horario';

@Injectable({
  providedIn: 'root',
})
export class Horarios {
  
  private apiUrl = 'http://localhost:3000/horarios';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Horario[]>(this.apiUrl)
  }

  addItem(item: Horario){
    return this.http.post<Horario>
  }

  updateItem(item: Horario){
    return this.http.put<Horario>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<Horario>(this.apiUrl + '/' + id)
  }

}
