import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";

@Injectable({
    providedIn: "root",
})
export class AuthService {
    constructor(private http: HttpClient) { }

    verifyEmail(body: { userType: number, email: string, password: string, reenteredPassword: string }) {
        return this.http.post(environment.AUTH_API + "verify-email", body);
    }

    signUp(body: {
        userType: number,
        userEmail: string,
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