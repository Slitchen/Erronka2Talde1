import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Ciclo } from '../../interface/ciclo';

@Injectable({
  providedIn: 'root',
})
export class Ciclos {
  private apiUrl = 'http://localhost:3000/ciclos';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Ciclo[]>(this.apiUrl)
  }

  addItem(item: Ciclo){
    return this.http.post<Ciclo>
  }

  updateItem(item: Ciclo){
    return this.http.put<Ciclo>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<Ciclo>(this.apiUrl + '/' + id)
  }

}
