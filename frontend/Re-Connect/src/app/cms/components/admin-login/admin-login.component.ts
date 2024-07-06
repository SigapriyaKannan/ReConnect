import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { MessageService } from 'primeng/api';
import { RouterLink } from '@angular/router';
import { AdminLoginService } from './admin-login.service';
import { RadioButtonModule } from 'primeng/radiobutton';
import { Router } from '@angular/router';

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
  templateUrl: './admin-login.component.html',
  styleUrls: ['./admin-login.component.scss']
})
export class AdminLoginComponent {
  loading: boolean = false;
  userCredentialsForm: FormGroup;
  activeStep: number = 0;
  roles = [{ roleId: 0, role: 'Referrer' }, { roleId: 1, role: 'Referent' }];
  selectedRole = 0;



  constructor(private messageService: MessageService, private loginService: AdminLoginService, private router: Router) {
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
        severity: 'error',
        summary: 'Error',
        detail: 'Error in form'
      });
      console.error('ERROR!');
    } else {
      const body = {
        userEmail: this.userCredentialsForm.controls['email'].value,
        password: this.userCredentialsForm.controls['password'].value
      };

      this.loginService.login(body).subscribe(
          (response: any) => {
            // Handle successful login response
            console.log('Login successful:', response);
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Login successful'

            });
            this.router.navigate(['/admin/dashboard'])
                .then(() => {
                  console.log('Navigation to admin page successful');
                })
                .catch(err => {
                  console.error('Navigation to admin page failed:', err);
                });

          },
          (error: any) => {
            // Handle login error
            console.error('Login failed:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Email or password is incorrect'
            });
          }
      );
    }
  }
}
