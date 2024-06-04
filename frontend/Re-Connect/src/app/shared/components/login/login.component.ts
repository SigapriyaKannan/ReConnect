import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from "primeng/card";
import { ButtonModule } from "primeng/button";
import { StepperModule } from "primeng/stepper";
import { InputTextModule } from "primeng/inputtext";
import { PasswordModule } from "primeng/password";
import { MessageService } from 'primeng/api';
import { RouterLink } from '@angular/router';
import { LoginService } from './login.service';
import { RadioButtonModule } from "primeng/radiobutton";

@Component({
  selector: 'rc-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, CardModule, ButtonModule, InputTextModule, PasswordModule, ReactiveFormsModule, StepperModule, RadioButtonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loading: boolean = false;
  userCredentialsForm: FormGroup;
  activeStep: number = 0;
  roles = [{ roleId: 0, role: "Referrer" }, { roleId: 1, role: "Referent" }];
  selectedRole = 0;


    constructor(private messageService: MessageService, private loginService: LoginService) {
      this.userCredentialsForm = new FormGroup({
        email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)]),
        password: new FormControl('', [Validators.required, Validators.minLength(5)]),
      });


  }

  onSubmit() {
    this.userCredentialsForm.markAllAsDirty();
    this.userCredentialsForm.markAllAsTouched();
    if (this.userCredentialsForm.invalid) {
      this.messageService.add({
        severity: "danger",
        summary: "Error",
        detail: "Error in form"
      });
      console.error("ERROR!");
    } else {
      const body = {
        email: this.userCredentialsForm.controls['email'].value,
        password: this.userCredentialsForm.controls['password'].value
      };

      this.loginService.login(body).subscribe(
        (response: any) => {
          // Handle successful login response
          console.log("Login successful:", response);
          this.messageService.add({
            severity: "success",
            summary: "Success",
            detail: "Login successful"
          });
        },
        (error: any) => {
          // Handle login error
          console.error("Login failed:", error);
          this.messageService.add({
            severity: "danger",
            summary: "Error",
            detail: "Email or password is incorrect"
          });
        }
      );
    }
  }
  
}
