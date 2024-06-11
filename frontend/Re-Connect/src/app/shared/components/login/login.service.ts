import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../../../environments/environment";
import { AuthService } from "../../../auth/auth.service";


@Injectable({
    providedIn: "root"
})
export class LoginService {

    constructor(private http: HttpClient, private authService: AuthService) { } // Inject the AuthService

    login(credentials: { userEmail: string; password: string }): Observable<any> {
        return this.http.post<any>(environment.API + "auth/login", credentials).pipe(
           
        );
    }
}