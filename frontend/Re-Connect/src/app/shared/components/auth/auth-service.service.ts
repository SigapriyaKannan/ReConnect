import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { tap } from 'rxjs/operators';
import { addMilliseconds, isBefore } from 'date-fns';
import { environment } from "../../../../environments/environment";
import { Router } from "@angular/router";
import { ToastService } from "../../services/toast.service";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    constructor(private http: HttpClient, private router: Router) { }

    login({ email, password }) {
        return this.http.post<any>(environment.AUTH_API + 'login', { email, password }).pipe(
            tap({
                next: (response) => this.setSession(response), // Corrected to use response directly
                error: (error) => console.error('Login error:', error)
            })
        );
    }

    private setSession(authResult: any) {
        const expiresAt = addMilliseconds(new Date(), authResult.body.expiresIn);
        sessionStorage.setItem('token', authResult.body.token);
        sessionStorage.setItem("expiresIn", expiresAt.toISOString());
    }

    logout() {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("expiresIn");
        // this.toastService.showSuccess("User Logged out successfully");
        this.router.navigate(["/login"]);
    }

    public isLoggedIn() {
        const expiration = sessionStorage.getItem("expiresIn");
        const token = sessionStorage.getItem("token");
        if (!expiration || !token) {
            console.log('No expiration found in sessionStorage.');
            return false;
        }
        const expiresAt = new Date(expiration);
        return isBefore(new Date(), expiresAt);
    }

    isLoggedOut() {
        return !this.isLoggedIn();
    }

    getUserDetails(token: string) {
        return this.http.get(environment.AUTH_API + "getUserDetails?token=" + token);
    }
}
