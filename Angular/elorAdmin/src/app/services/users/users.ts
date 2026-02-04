import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../../interface/user';

@Injectable({
  providedIn: 'root',
})
export class Users {

  private apiUrl = 'http://localhost:3000/users';  
  http: HttpClient = inject(HttpClient);

  getItems(){
    return this.http.get<User[]>(this.apiUrl)
  }

  addItem(item: User){
    return this.http.post<User>
  }

  updateItem(item: User){
    return this.http.put<User>(this.apiUrl + '/' + item.id, item)
  }

  deleteItem(id: string){
    return this.http.delete<User>(this.apiUrl + '/' + id)
  }
  

}
