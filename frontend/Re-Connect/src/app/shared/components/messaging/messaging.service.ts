import { HttpClient, HttpParams } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import { Injectable } from "@angular/core";

@Injectable()
export class MessagingService {
    constructor(private http: HttpClient) { }

    getAcceptedConnections() {
        return this.http.get<any[]>(environment.API + "getAcceptedConnections");
    }

    getChatHistory(email: string) {
        let params = new HttpParams();
        params = params.append("email", email);
        return this.http.get<any[]>(environment.API + "getChatHistory", { params: params });
    }
}