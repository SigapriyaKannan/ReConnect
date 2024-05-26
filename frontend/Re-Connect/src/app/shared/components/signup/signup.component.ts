import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CardModule } from "primeng/card";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { PasswordModule } from "primeng/password";
import { DividerModule } from "primeng/divider";
import { StepperModule } from "primeng/stepper";
import { DropdownModule } from "primeng/dropdown";
import { MultiSelectModule } from "primeng/multiselect";

import { SignUpService } from './signup.service';
import { CompanyService } from '../../services/company.service';
import { ExperienceService } from '../../services/experience.service';
import { SkillsService } from '../../services/skills.service';
import { CountryService } from '../../services/country.service';
import { CityService } from '../../services/city.service';

@Component({
  selector: 'rc-signup',
  standalone: true,
  imports: [CardModule, ButtonModule, InputTextModule, PasswordModule, DividerModule, ReactiveFormsModule, StepperModule, DropdownModule, MultiSelectModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  form: FormGroup;
  loading: boolean = false;
  listOfCompanies: string[] = [
    "Apple",
    "Google",
    "Microsoft",
    "Amazon",
    "Facebook",
    "Tesla"
  ];;
  listOfExperiences: string[] = [
    "0-1 years",
    "1-3 years",
    "3-5 years",
    "5-10 years",
    "10+ years"
  ];
  listOfCountries: string[] = [
    "United States",
    "Canada",
    "United Kingdom",
    "Germany",
    "Japan",
    "Australia",
    "Brazil",
    "South Africa",
    "India",
    "China"
  ];
  listOfCities: string[] = [
    "New York",
    "Los Angeles",
    "London",
    "Paris",
    "Tokyo",
    "Sydney",
    "Rio de Janeiro",
    "Cape Town",
    "Mumbai",
    "Shanghai"
  ];
  listOfSkills: string[] = [
    "JavaScript",
    "Python",
    "HTML",
    "CSS",
    "React",
    "Node.js",
    "SQL",
    "Java",
    "AWS",
    "Docker"
  ];


  constructor(private signUpService: SignUpService, private companyService: CompanyService, private experienceService: ExperienceService, private skillsService: SkillsService, private countryService: CountryService, private cityService: CityService) {
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(8)]),
      company: new FormControl(null, [Validators.required]),
      experience: new FormControl(null, [Validators.required]),
      skills: new FormControl([], [Validators.required, Validators.min(1), Validators.max(5)]),
      country: new FormControl(null, [Validators.required]),
      city: new FormControl(null, [Validators.required])
    });
  }

  onSubmit() {
    this.form.markAllAsTouched();
    console.log(this.form.valid);
  }
}
