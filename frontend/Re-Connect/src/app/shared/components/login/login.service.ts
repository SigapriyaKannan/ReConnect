import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../../../../environments/environment";

@Injectable({
    providedIn: "root"
})
export class LoginService {

    constructor(private http: HttpClient) { } // Inject the AuthService

    login(credentials: { email: string; password: string }): Observable<any> {
        return this.http.post<any>(environment.API + "", credentials).pipe(

        );
    }
}