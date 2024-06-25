import { Component } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessageService } from 'primeng/api';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'rc-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    CardModule,
    ButtonModule,
    InputTextModule,
    ReactiveFormsModule,
    HttpClientModule,
    ToastModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;

  constructor(
      private messageService: MessageService,
      private authService: AuthService,
      private router: Router
  ) {
    this.forgotPasswordForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)])
    });
  }

  onSubmit() {
    this.forgotPasswordForm.markAllAsDirty();
    this.forgotPasswordForm.markAllAsTouched();
    if (this.forgotPasswordForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Please provide a valid email.'
      });
      console.error('ERROR!');
    } else {
      const email = this.forgotPasswordForm.controls['email'].value;

      this.authService.forgotPassword(email).subscribe(
          (response: any) => {
            console.log('Reset email sent:', response);
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Password reset email sent successfully'
            });
          },
          (error: any) => {
            console.error('Reset email failed:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to send password reset email'
            });
          }
      );
    }
  }
}
