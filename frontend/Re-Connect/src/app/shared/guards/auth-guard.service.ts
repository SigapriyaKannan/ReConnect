import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateChildFn, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';



export const canActivatePage: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (!authService.isLoggedIn()) {
    return true; // User is not authenticated, allow access to route
  } else {
    return router.createUrlTree(["/", "homepage"])
  }
}

export const canActivateChildPage: CanActivateChildFn = (childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (authService.isLoggedIn()) {
    return true; // User is authenticated, allow access to route
  } else {
    return router.createUrlTree(['/login']); // User is authenticated, redirect to login page
  }
};