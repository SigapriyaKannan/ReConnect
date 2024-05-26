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
import { Company, CompanyService } from '../../services/company.service';
import { Experience, ExperienceService } from '../../services/experience.service';
import { Skill, SkillsService } from '../../services/skills.service';
import { Country, CountryService } from '../../services/country.service';
import { City, CityService } from '../../services/city.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'rc-signup',
  standalone: true,
  imports: [CardModule, ButtonModule, InputTextModule, PasswordModule, DividerModule, ReactiveFormsModule, StepperModule, DropdownModule, MultiSelectModule],
  // providers: [MessageService],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  userCredentialsForm: FormGroup;
  userDetailsForm: FormGroup;
  loading: boolean = false;
  activeStep: number = 0;
  listOfCompanies: Company[] = [
    {
      "companyId": 1,
      "companyName": "Apple"
    },
    {
      "companyId": 2,
      "companyName": "Google"
    },
    {
      "companyId": 3,
      "companyName": "Amazon"
    },
    {
      "companyId": 4,
      "companyName": "Microsoft"
    },
    {
      "companyId": 5,
      "companyName": "Facebook"
    }
  ];

  listOfExperiences: Experience[] = [
    {
      "experienceId": 1,
      "experienceName": "Entry Level"
    },
    {
      "experienceId": 2,
      "experienceName": "Mid Level"
    },
    {
      "experienceId": 3,
      "experienceName": "Senior Level"
    },
    {
      "experienceId": 4,
      "experienceName": "Executive Level"
    }
  ]

  listOfCountries: Country[] = [
    {
      "countryId": 1,
      "countryName": "United States"
    },
    {
      "countryId": 2,
      "countryName": "Canada"
    },
    {
      "countryId": 3,
      "countryName": "United Kingdom"
    },
    {
      "countryId": 4,
      "countryName": "Germany"
    },
    {
      "countryId": 5,
      "countryName": "Australia"
    }
  ];

  listOfCities: City[] = [
    {
      "cityId": 1,
      "cityName": "New York"
    },
    {
      "cityId": 2,
      "cityName": "Los Angeles"
    },
    {
      "cityId": 3,
      "cityName": "Chicago"
    },
    {
      "cityId": 4,
      "cityName": "San Francisco"
    },
    {
      "cityId": 5,
      "cityName": "Miami"
    }
  ];

  listOfSkills: Skill[] = [
    {
      "skillId": 1,
      "skillName": "Java"
    },
    {
      "skillId": 2,
      "skillName": "Python"
    },
    {
      "skillId": 3,
      "skillName": "JavaScript"
    },
    {
      "skillId": 4,
      "skillName": "SQL"
    },
    {
      "skillId": 5,
      "skillName": "HTML/CSS"
    },
    {
      "skillId": 6,
      "skillName": "C++"
    },
    {
      "skillId": 7,
      "skillName": "Ruby"
    },
    {
      "skillId": 8,
      "skillName": "PHP"
    },
    {
      "skillId": 9,
      "skillName": "React"
    },
    {
      "skillId": 10,
      "skillName": "Angular"
    }
  ];



  constructor(private messageService: MessageService, private signUpService: SignUpService, private companyService: CompanyService, private experienceService: ExperienceService, private skillsService: SkillsService, private countryService: CountryService, private cityService: CityService) {
    this.userCredentialsForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)]),
      password: new FormControl('', [Validators.required, Validators.minLength(5)]),
      confirmPassword: new FormControl('', [Validators.required, Validators.minLength(5)]),
    });

    this.userDetailsForm = new FormGroup({
      company: new FormControl(null, [Validators.required]),
      experience: new FormControl(null, [Validators.required]),
      skills: new FormControl([], [Validators.required, Validators.min(1), Validators.max(5)]),
      country: new FormControl(null, [Validators.required]),
      city: new FormControl(null, [Validators.required])
    });

    // this.countryService.getCountries().subscribe((response: Country[]) => {
    //   this.listOfCountries = response.countries
    // });
    // this.companyService.getCompanies().subscribe((response: Company[]) => {
    //   this.listOfCompanies = response.companies
    // });
    // this.experienceService.getExperiences().subscribe((response: Experience[]) => {
    //   this.listOfExperiences = response.experiences
    // });
    // this.skillsService.getSkills().subscribe((response: Skill[]) => {
    //   this.listOfSkills = response.skills
    // });
    // this.userDetailsForm.controls['country'].valueChanges.pipe(
    //   switchMap(countryId => this.cityService.getCities(countryId))
    // ).subscribe((response: City[]) => {
    //   this.listOfCities = response
    // })
  }

  onCheckForm(form: FormGroup, step: number) {
    form.markAllAsDirty();
    form.markAllAsTouched();

    if (!this.checkPasswordsMatch()) {
      this.messageService.add({
        severity: "danger",
        summary: "Passwords Mismatch",
        detail: "Passwords do not match!"
      })
      console.error("Passwords do not match!");
      return;
    }

    if (form.invalid) {
      this.messageService.add({
        severity: "error",
        summary: "Form Error",
        detail: "Error in form!"
      })
    } else {
      this.activeStep = step + 1;
    }
  }

  onSubmit() {
    this.userCredentialsForm.markAllAsDirty();
    this.userCredentialsForm.markAllAsTouched();
    this.userDetailsForm.markAllAsTouched();
    this.userDetailsForm.markAllAsDirty();
    if (this.userCredentialsForm.invalid || this.userDetailsForm.invalid) {
      this.messageService.add({
        severity: "danger",
        summary: "Error",
        detail: "Error in form"
      })
      console.error("ERROR!");
    } else {
      const body = {
        email: this.userCredentialsForm.controls['email'].value,
        password: this.userCredentialsForm.controls['password'].value,
        company: this.userDetailsForm.controls['company'].value,
        experience: this.userDetailsForm.controls['experience'].value,
        skills: this.userDetailsForm.controls['skills'].value,
        country: this.userDetailsForm.controls['country'].value,
        city: this.userDetailsForm.controls['city'].value
      }

      console.table(body);
    }
  }

  private checkPasswordsMatch(): boolean {
    return this.userCredentialsForm.controls['password'].value === this.userCredentialsForm.controls['confirmPassword'].value;
  }
}
