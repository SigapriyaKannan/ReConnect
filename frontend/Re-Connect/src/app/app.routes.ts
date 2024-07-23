import { Routes } from '@angular/router';
import { LoginComponent } from './shared/components/login/login.component';
import { SignupComponent } from './shared/components/signup/signup.component';
import { AdminLayoutComponent } from './cms/components/admin-layout/admin-layout.component';
import { AdminDashboardComponent } from './cms/components/admin-dashboard/admin-dashboard.component';
import { AdminUsersComponent } from './cms/components/admin-users/admin-users.component';
import { AdminCountriesComponent } from './cms/components/admin-countries/admin-countries.component';
import { AdminCitiesComponent } from './cms/components/admin-cities/admin-cities.component';
import { AdminSkillsComponent } from './cms/components/admin-skills/admin-skills.component';
import { AdminCompaniesComponent } from './cms/components/admin-companies/admin-companies.component';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { HomepageComponent } from './shared/components/homepage/homepage.component';
import { NotificationsComponent } from './shared/components/notifications/notifications.component';
import { RequestsComponent } from './shared/components/requests/requests.component';
import { MessagingComponent } from './shared/components/messaging/messaging.component';
import { ForgotPasswordComponent } from "./shared/components/forgot-password/forgot-password.component";
import { ResetPasswordComponent } from "./shared/components/reset-password/reset-password.component";
import { AdminSkillDomainComponent } from "./cms/components/admin-skill-domain/admin-skill-domain.component";
import { AdminLoginComponent } from "./cms/components/admin-login/admin-login.component";
import { canActivateChildPage, canActivatePage } from "./shared/guards/auth-guard.service";
import { UserResolver } from './shared/resolvers/user-resolver.service';
import { ProfileComponent } from "./shared/components/profile/profile.component";

export const routes: Routes = [
    {
        path: "login",
        component: LoginComponent,
        canActivate: [canActivatePage]
    },
    {
        path: "sign-up",
        component: SignupComponent,
        canActivate: [canActivatePage]
    },
    {
        path: "forgot-password",
        component: ForgotPasswordComponent,
        canActivate: [canActivatePage]
    },
    {
        path: "reset-password",
        component: ResetPasswordComponent,
        canActivate: [canActivatePage]
    },
    {
        path: "",
        component: LayoutComponent,
        canActivateChild: [canActivateChildPage],
        resolve: {
            user: UserResolver
        },
        children: [
            {
                path: "homepage",
                component: HomepageComponent

            },
            {
                path: "notifications",
                component: NotificationsComponent
            },
            {
                path: "requests",
                component: RequestsComponent
            },
            {
                path: "messages",
                component: MessagingComponent
            },
            {
                path: "profile",
                component: ProfileComponent,
                data: { showEdit: true }
            },
            {
                path: "other-profile",
                component: ProfileComponent,
                data: { showEdit: false }
            },
            {
                path: "",
                pathMatch: "full",
                redirectTo: "homepage"
            }
        ]
    },
    {
        path: "admin",
        component: AdminLayoutComponent,
        children: [
            {
                path: "dashboard",
                component: AdminDashboardComponent
            },
            {
                path: "users",
                component: AdminUsersComponent
            },
            {
                path: "countries",
                component: AdminCountriesComponent
            },
            {
                path: "cities",
                component: AdminCitiesComponent
            },
            {
                path: "skills",
                component: AdminSkillsComponent
            },
            {
                path: "skill-domain",
                component: AdminSkillDomainComponent
            },
            {
                path: "companies",
                component: AdminCompaniesComponent
            },
            {
                path: "users",
                component: AdminUsersComponent
            },
            {
                path: "",
                redirectTo: "dashboard",
                pathMatch: "full"
            }
        ]
    },
    {
        path: "**",
        redirectTo: "homepage",
        pathMatch: "full"
    }
];
