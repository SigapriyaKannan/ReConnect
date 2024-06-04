import { Routes } from '@angular/router';
import { LoginComponent } from './shared/components/login/login.component';
import { SignupComponent } from './shared/components/signup/signup.component';
import { ForgotPasswordComponent } from './shared/components/forgot-password/forgot-password.component';

export const routes: Routes = [
    {
        path: "login",
        component: LoginComponent
    },
    {
        path: "sign-up",
        component: SignupComponent
    },
    {
        path:"forgot-password",
        component: ForgotPasswordComponent
    },
    {
        path: "",
        redirectTo: "sign-up",
        pathMatch: "full"
    },
    {
        path: "**",
        redirectTo: "sign-up",
        pathMatch: "full"
    }
];
