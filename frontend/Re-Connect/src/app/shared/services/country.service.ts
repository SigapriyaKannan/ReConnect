import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface Country {
  countryId: number,
  countryName: String
}

@Injectable({
  providedIn: 'root'
})
export class CountryService {

  constructor(private http: HttpClient) { }

  getCountries() {
    return this.http.get<Country[]>("");
  }
}
