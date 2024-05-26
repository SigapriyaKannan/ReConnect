import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface Skill {
  skillId: number,
  skillName: String
}

@Injectable({
  providedIn: 'root'
})
export class SkillsService {

  constructor(private http: HttpClient) { }

  getSkills() {
    return this.http.get<Skill[]>("");
  }
}
