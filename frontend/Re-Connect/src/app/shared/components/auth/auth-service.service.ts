import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { tap } from 'rxjs/operators';
import { addMilliseconds, isBefore } from 'date-fns';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient) {}

    login(email: string, password: string) {
        return this.http.post<any>('http://localhost:8080/auth/login', { email, password }).pipe(
            tap({
                next: (response) => this.setSession(response), // Corrected to use response directly
                error: (error) => console.error('Login error:', error)
            })
        );
    }

    public setSession(authResult: any) {
        console.log('authResult:', authResult);
        const expiresAt = addMilliseconds(new Date(), authResult.body.expiresIn);
        console.log('expiresAt:', expiresAt.toISOString());     
        localStorage.setItem('token', authResult.body.token);
        localStorage.setItem("expiresIn", expiresAt.toISOString());
    }

    logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("expiresIn");
    }

    public isLoggedIn() {
        const expiration = localStorage.getItem("expiresIn");
        if (!expiration) {
            console.log('No expiration found in localStorage.');
            return false;
        }
        const expiresAt = new Date(expiration);
        console.log('Current date:', new Date());
        console.log('Expires at:', expiresAt);
        return isBefore(new Date(), expiresAt);
    }

    isLoggedOut() {
        return !this.isLoggedIn();
    }   
}
