import { Component } from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {
    FormControl,
    FormGroup,
    Validators,
    ValidatorFn,
    FormsModule,
    ReactiveFormsModule,
    AbstractControl,
    ValidationErrors,
} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {AuthService} from '../../services/auth.service';
import {ToastModule} from "primeng/toast";
import {ButtonModule} from "primeng/button";
import {PasswordModule} from "primeng/password";
import {CardModule} from "primeng/card";
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

@Component({
    selector: 'rc-reset-password',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        RouterLink,
        CardModule,
        ButtonModule,
        InputTextModule,
        ReactiveFormsModule,
        PasswordModule,
        HttpClientModule,
        ToastModule
    ],
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
    resetPasswordForm: FormGroup;
    token: string = '';

    constructor(
        private route: ActivatedRoute,
        private messageService: MessageService,
        private authService: AuthService,
        private router: Router
    ) {
        this.route.queryParams.subscribe(params => {
            this.token = params['token'] || '';
        });

        this.resetPasswordForm = new FormGroup({
            email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(50)]),
            newPassword: new FormControl('', [Validators.required, Validators.minLength(5)]),
            reenterPassword: new FormControl('', [Validators.required, Validators.minLength(5)])
        }, { validators: this.passwordsMatchValidator });
    }

    passwordsMatchValidator: ValidatorFn = (form: AbstractControl): ValidationErrors | null => {
        const password = form.get('newPassword')?.value;
        const confirmPassword = form.get('reenterPassword')?.value;

        return password === confirmPassword ? null : { mismatch: true };
    }

    onSubmit() {
        this.resetPasswordForm.markAllAsDirty();
        this.resetPasswordForm.markAllAsTouched();
        if (this.resetPasswordForm.invalid) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Please provide valid information.'
            });
            console.error('ERROR!');
        } else {
            const email = this.resetPasswordForm.controls['email'].value;
            const newPassword = this.resetPasswordForm.controls['newPassword'].value;

            this.authService.resetPassword(this.token, newPassword).subscribe(
                (response: any) => {
                    console.log('Password reset successful:', response);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Success',
                        detail: 'Password reset successfully'
                    });
                    this.router.navigate(['/login'])
                        .then(() => {
                            console.log('Navigation to login page successful');
                        })
                        .catch(err => {
                            console.error('Navigation to login page failed:', err);
                        });
                },
                (error: any) => {
                    console.error('Password reset failed:', error);
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: 'Failed to reset password'
                    });
                }
            );
        }
    }
}
