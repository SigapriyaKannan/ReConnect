import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from "primeng/card";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { PasswordModule } from "primeng/password";
import { DividerModule } from "primeng/divider";
import { StepperModule } from "primeng/stepper";
import { DropdownModule } from "primeng/dropdown";
import { MultiSelectModule } from "primeng/multiselect";
import { RadioButtonModule } from "primeng/radiobutton";

import { SignUpService } from './signup.service';
import { Company, CompanyService } from '../../services/company.service';
import { Experience, ExperienceService } from '../../services/experience.service';
import { Skill, SkillsService } from '../../services/skills.service';
import { Country, CountryService } from '../../services/country.service';
import { City, CityService } from '../../services/city.service';
import { ROLES } from '../constants/roles';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { switchMap } from 'rxjs';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'rc-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CardModule, ButtonModule, InputTextModule, PasswordModule, DividerModule, ReactiveFormsModule, StepperModule, DropdownModule, MultiSelectModule, RadioButtonModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  userCredentialsForm: FormGroup;
  userDetailsForm: FormGroup;
  loading: boolean = false;
  activeStep: number = 0;
  roles = [{ roleId: 0, role: "Admin" }, { roleId: 1, role: "Referrer" }, { roleId: 2, role: "Referent" }];
  selectedRole = 1;
  verificationPending: boolean = false;
  isCreatingUser: boolean = false;

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
  listOfCompanies: Company[] = [];
  listOfCountries: Country[] = [];
  listOfCities: City[] = [];
  listOfSkills: Skill[] = [];

  constructor(private router: Router, private toastService: ToastService, private signUpService: SignUpService, private authService: AuthService, private companyService: CompanyService, private experienceService: ExperienceService, private skillsService: SkillsService, private countryService: CountryService, private cityService: CityService) {
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

    this.countryService.getCountries().subscribe((response: any) => {
      this.listOfCountries = response['body'];
    });
    this.companyService.getCompanies().subscribe((response: Company[]) => {
      this.listOfCompanies = response['body'];
    });
    // this.experienceService.getExperiences().subscribe((response: Experience[]) => {
    //   this.listOfExperiences = response['body'];
    // });
    this.skillsService.getSkills().subscribe((response: Skill[]) => {
      this.listOfSkills = response['body'];
    });

    this.userDetailsForm.controls['country'].valueChanges.pipe(
      switchMap(countryId => this.cityService.getCities(countryId))
    ).subscribe((response: City[]) => {
      this.listOfCities = response['body'];
    })
  }

  onCheckForm(form: FormGroup, step: number) {
    form.markAllAsDirty();
    form.markAllAsTouched();

    if (!this.checkPasswordsMatch()) {
      this.toastService.showError("Passwords do not match!")
      console.error("Passwords do not match!");
      return;
    }

    if (form.invalid) {

    } else {
      this.verificationPending = true;
      const body = {
        userType: this.selectedRole,
        email: form.get('email')?.value,
        password: form.get('password')?.value,
        reenteredPassword: form.get('confirmPassword')?.value
      }
      this.authService.verifyEmail(body).subscribe(response => {
        this.verificationPending = false;
        this.activeStep = step + 1;
      }, (error) => {
        this.verificationPending = false;
        this.toastService.showError("Email is already registered with us!");
      }, () => {
        this.verificationPending = false;
      })
    }
  }

  onSubmit() {
    this.userCredentialsForm.markAllAsDirty();
    this.userCredentialsForm.markAllAsTouched();
    this.userDetailsForm.markAllAsTouched();
    this.userDetailsForm.markAllAsDirty();
    if (this.userCredentialsForm.invalid || this.userDetailsForm.invalid) {
      this.toastService.showFormError();
    } else {
      this.isCreatingUser = true;
      const body = {
        userType: this.selectedRole,
        email: this.userCredentialsForm.controls['email'].value,
        password: this.userCredentialsForm.controls['password'].value,
        reenteredPassword: this.userCredentialsForm.controls['confirmPassword'].value,
        company: this.userDetailsForm.controls['company'].value,
        experience: this.userDetailsForm.controls['experience'].value,
        skills: this.userDetailsForm.controls['skills'].value,
        country: this.userDetailsForm.controls['country'].value,
        city: this.userDetailsForm.controls['city'].value,
        resume: "",
        profile: ""
      }

      this.authService.signUp(body).subscribe(response => {
        this.isCreatingUser = false;
        this.toastService.showSuccess("User created successfully");
        this.router.navigate(["/", "login"]);
      }, (error) => {
        this.isCreatingUser = false;
      })

      console.table(body);
    }
  }

  private checkPasswordsMatch(): boolean {
    return this.userCredentialsForm.controls['password'].value === this.userCredentialsForm.controls['confirmPassword'].value;
  }
}
