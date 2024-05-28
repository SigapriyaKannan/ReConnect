import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface Experience {
  experienceId: number,
  experienceName: String
}

@Injectable({
  providedIn: 'root'
})
export class ExperienceService {

  constructor(private http: HttpClient) { }

  getExperiences() {
    return this.http.get<Experience[]>("");
  }
}
