import { inject, Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, ResolveFn, Router, RouterStateSnapshot } from "@angular/router";
import { AuthService } from "../services/auth.service";

export const UserResolver: ResolveFn<any> = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const token = localStorage.getItem("token");
    if (!token) {
        return router.navigateByUrl("/login");
    }
    return authService.getUserDetails(token!);

}