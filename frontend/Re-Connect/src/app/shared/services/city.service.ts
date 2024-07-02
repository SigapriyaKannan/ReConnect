import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

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
    return this.http.get<City[]>(environment.API + `cities/getAllCities/${countryId}`);
  }
}
