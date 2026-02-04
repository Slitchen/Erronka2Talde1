import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Reunion } from '../../interface/reunion';

@Injectable({
  providedIn: 'root',
})
export class Reuniones {

  private apiUrl = 'http://localhost:3000/reuniones';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Reunion[]>(this.apiUrl)
  }

  addItem(item: Reunion){
    return this.http.post<Reunion>
  }

  updateItem(item: Reunion){
    return this.http.put<Reunion>(this.apiUrl + '/' + item.id_reunion, item)
  }

  deleteItem(id: string){
    return this.http.delete<Reunion>(this.apiUrl + '/' + id)
  }
  
}
