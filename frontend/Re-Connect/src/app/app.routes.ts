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
                loadChildren: () => import("./shared/components/homepage/homepage.component").then(m => m.HomepageComponent)

            },
            {
                path: "notifications",
                loadChildren: () => import("./shared/components/notifications/notifications.component").then(m => m.NotificationsComponent)
            },
            {
                path: "requests",
                loadChildren: () => import("./shared/components/requests/requests.component").then(m => m.RequestsComponent)
            },
            {
                path: "messages",
                loadChildren: () => import("./shared/components/messaging/messaging.component").then(m => m.MessagingComponent)
            },
            {
                path: "profile/:id",
                loadChildren: () => import("./shared/components/profile/profile.component").then(m => m.ProfileComponent),
                data: { showEdit: true }
            },
            {
                path: "other-profile/:id",
                loadChildren: () => import("./shared/components/profile/profile.component").then(m => m.ProfileComponent),
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
                loadChildren: () => import("./cms/components/admin-dashboard/admin-dashboard.component").then(m => m.AdminDashboardComponent),
                component: AdminDashboardComponent
            },
            {
                path: "users",
                loadChildren: () => import("./cms/components/admin-users/admin-users.component").then(m => m.AdminUsersComponent),
                component: AdminUsersComponent
            },
            {
                path: "countries",
                loadChildren: () => import("./cms/components/admin-countries/admin-countries.component").then(m => m.AdminCountriesComponent),
                component: AdminCountriesComponent
            },
            {
                path: "cities",
                loadChildren: () => import("./cms/components/admin-cities/admin-cities.component").then(m => m.AdminCitiesComponent),
                component: AdminCitiesComponent
            },
            {
                path: "skills",
                loadChildren: () => import("./cms/components/admin-skills/admin-skills.component").then(m => m.AdminSkillsComponent),
                component: AdminSkillsComponent
            },
            {
                path: "skill-domain",
                loadChildren: () => import("./cms/components/admin-skill-domain/admin-skill-domain.component").then(m => m.AdminSkillDomainComponent),
                component: AdminSkillDomainComponent
            },
            {
                path: "companies",
                loadChildren: () => import("./cms/components/admin-companies/admin-companies.component").then(m => m.AdminCompaniesComponent),
                component: AdminCompaniesComponent
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
