
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserNameTypeIdDTO {
  userId: number;
  userName: string;
  typeId: number;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = environment.API + 'users'; 

  constructor(private http: HttpClient) { }

  getUserNamesAndTypeIdsByUserType(typeName: string): Observable<UserNameTypeIdDTO[]> {
    const params = new HttpParams().set('typeName', typeName);
    return this.http.get<UserNameTypeIdDTO[]>(`${this.apiUrl}/usernames-and-typeids`, { params, });
  }

  deleteUser(userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${userId}`);
  }
}