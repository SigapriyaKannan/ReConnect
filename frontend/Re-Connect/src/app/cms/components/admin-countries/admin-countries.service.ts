import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../environments/environment";

export class AdminCountriesService {
    constructor(private http: HttpClient) { }

    getCountries() {
        this.http.get(environment.API + "getCountries")
    }
}