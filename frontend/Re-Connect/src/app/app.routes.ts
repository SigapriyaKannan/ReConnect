import { Routes } from '@angular/router';
import { LoginComponent } from './components/shared/login/login.component';
import { SignupComponent } from './components/shared/signup/signup.component';

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
        path: "",
        redirectTo: "login",
        pathMatch: "full"
    }
];
