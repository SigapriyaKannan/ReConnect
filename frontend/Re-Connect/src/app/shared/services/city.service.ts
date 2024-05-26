import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface City {
  cityId: number,
  cityName: string
}

@Injectable({
  providedIn: 'root'
})
export class CityService {

  constructor(private http: HttpClient) { }

  getCities(countryId: number) {
    return this.http.get<City[]>("");
  }
}
