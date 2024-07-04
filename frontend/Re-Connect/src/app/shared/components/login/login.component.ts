import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { RouterLink } from '@angular/router';
import { LoginService } from './login.service';
import { RadioButtonModule } from 'primeng/radiobutton';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'rc-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    CardModule,
    ButtonModule,
    InputTextModule,
    PasswordModule,
    ReactiveFormsModule,
    RadioButtonModule,
    HttpClientModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loading: boolean = false;
  userCredentialsForm: FormGroup;
  activeStep: number = 0;
  roles = [{ roleId: 0, role: "Admin" }, { roleId: 1, role: "Referrer" }, { roleId: 2, role: "Referent" }];
  selectedRole = 0;



  constructor(private toastService: ToastService, private loginService: LoginService, private router: Router) {
    this.userCredentialsForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)]),
      password: new FormControl('', [Validators.required, Validators.minLength(5)]),
    });
  }




  onSubmit() {
    this.userCredentialsForm.markAllAsDirty();
    this.userCredentialsForm.markAllAsTouched();
    if (this.userCredentialsForm.invalid) {
      this.toastService.showFormError();
      console.error('ERROR!');
    } else {
      const body = {
        email: this.userCredentialsForm.controls['email'].value,
        password: this.userCredentialsForm.controls['password'].value
      };

      this.loginService.login(body).subscribe(
        (response: any) => {
          // Handle successful login response
          this.toastService.showSuccess('Login successful');

        },
        (error: any) => {
          // Handle login error
          console.error('Login failed:', error);
          this.toastService.showError('Email or password is incorrect');
        }
      );
    }
  }
}
