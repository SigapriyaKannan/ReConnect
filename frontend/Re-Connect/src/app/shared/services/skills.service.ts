import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

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
    return this.http.get<Skill[]>(environment.API + "skills/getAllSkills");
  }
}
