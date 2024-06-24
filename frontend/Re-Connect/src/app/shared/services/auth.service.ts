import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private baseUrl = environment.API;

    constructor(private http: HttpClient) {}

    forgotPassword(email: string): Observable<any> {
        return this.http.post(`${this.baseUrl}auth/forgotPassword`, { email }, { responseType: 'text' });
    }

    resetPassword(token: string, newPassword: string): Observable<any> {
        return this.http.post(`${this.baseUrl}auth/resetPassword`, {
            token,
            newPassword
        }, { responseType: 'text' });
    }
}
