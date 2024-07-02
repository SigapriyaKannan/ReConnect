import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export interface Company {
  companyId: number,
  companyName: string
}

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  constructor(private http: HttpClient) { }

  getCompanies() {
    return this.http.get<Company[]>(environment.API + "companies/getAllCompanies");
  }
}
