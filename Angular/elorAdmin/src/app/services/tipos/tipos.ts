import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Tipo } from '../../interface/tipo';

@Injectable({
  providedIn: 'root',
})
export class Tipos {
  
  private apiUrl = 'http://localhost:3000/tipos';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Tipo[]>(this.apiUrl)
  }

  addItem(item: Tipo){
    return this.http.post<Tipo>
  }

  updateItem(item: Tipo){
    return this.http.put<Tipo>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<Tipo>(this.apiUrl + '/' + id)
  }
  
}
