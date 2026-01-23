import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Matriculacion } from '../../interface/matriculacion';

@Injectable({
  providedIn: 'root',
})
export class Matriculaciones {
  
  private apiUrl = 'http://localhost:3000/matriculaciones';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Matriculacion[]>(this.apiUrl)
  }

  addItem(item: Matriculacion){
    return this.http.post<Matriculacion>
  }

  updateItem(item: Matriculacion){
    return this.http.put<Matriculacion>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<Matriculacion>(this.apiUrl + '/' + id)
  }


}
