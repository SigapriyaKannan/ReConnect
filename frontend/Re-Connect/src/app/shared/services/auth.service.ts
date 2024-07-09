
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private baseUrl = environment.AUTH_API;

    constructor(private http: HttpClient) { }

    forgotPassword(email: string): Observable<any> {
        return this.http.post(`${this.baseUrl}auth/forgotPassword`, { email }, { responseType: 'text' });
    }

    resetPassword(token: string, newPassword: string): Observable<any> {
        return this.http.post(`${this.baseUrl}auth/resetPassword`, {
            token,
            newPassword
        }, { responseType: 'text' });
    }

    verifyEmail(body: { userType: number, email: string, password: string, reenteredPassword: string }) {
        return this.http.post(environment.AUTH_API + "verify-email", body);
    }

    signUp(body: FormData) {
        const headers = new HttpHeaders();
        return this.http.post(environment.AUTH_API + "signup", body, { headers });
    }
}
