import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HomepageService {
  private apiUrl = environment.API;

  constructor(private http: HttpClient) {}

  searchUsers(username: string): Observable<any> {
    return this.http.get(`${this.apiUrl}users?username=${username}`).pipe(
      map(this.processUserResponse)
    );
  }

  searchCompanyUsers(companyName: string): Observable<any> {
    return this.http.get(`${this.apiUrl}companies/users?companyName=${companyName}`).pipe(
      map(this.processResponse)
    );
  }

  private processUserResponse(response: any): any {
    if (response && response.status === 200 && response.body && Array.isArray(response.body)) {
      return {
        statusCode: response.status,
        message: response.message,
        data: response.body 
      };
    } else {
      console.error('Unexpected API response format', response);
      return { statusCode: 404, message: 'No results found', data: [] };
    }
  }

  private processResponse(response: any): any {
    if (response && response.status === 200 && response.body && Array.isArray(response.body)) {
      return {
        statusCode: response.status,
        message: response.message,
        data: response.body
      };
    } else {
      console.error('Unexpected API response format', response);
      return { statusCode: 404, message: 'No results found', data: [] };
    }
  }
}