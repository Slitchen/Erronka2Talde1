import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Modulo } from '../../interface/modulo';

@Injectable({
  providedIn: 'root',
})
export class Modulos {
    
  private apiUrl = 'http://localhost:3000/modulos';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<Modulo[]>(this.apiUrl)
  }

  addItem(item: Modulo){
    return this.http.post<Modulo>
  }

  updateItem(item: Modulo){
    return this.http.put<Modulo>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<Modulo>(this.apiUrl + '/' + id)
  }

}
