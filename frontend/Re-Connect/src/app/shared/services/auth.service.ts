
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

    signUp(body: {
        userType: number,
        email: string,
        password: string,
        reenteredPassword: string,
        company: number,
        experience: number,
        skills: [
            number
        ],
        country: number,
        city: number,
        resume: string,
        profile: string
    }) {
        return this.http.post(environment.AUTH_API + "signup", body);
    }
}
