import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateChildFn, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';



export const canActivatePage: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (!authService.isLoggedIn()) {
    return true; // User is not authenticated, allow access to route
  } else {
    return router.createUrlTree(["/", "user"])
  }
}

export const RoleGuard: CanActivateChildFn = async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem("token");
  if (!token) {
    return router.createUrlTree(["/", "login"]);
  }

  const user = await authService.getUserDetails(token)
  const role = user['role'];

  if (role > 0) {
    const urlTree = router.createUrlTree(['/user']);
    router.navigateByUrl(urlTree);
    return false; // Prevent access to the empty route
  } else if (role === 0) {
    const urlTree = router.createUrlTree(['/admin']);
    router.navigateByUrl(urlTree);
    return false; // Prevent access to the empty route
  }

  return true;
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

export const canActivateUserPages: CanActivateChildFn = (childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem("token");
  if (!token) {
    return router.createUrlTree(["/", "login"]);
  }

  return authService.getUserDetails(token).pipe(
    map(
      user => {
        const role = user['role'];
        if (role > 0) {
          return true; // Prevent access to the empty route
        } else if (role === 0) {
          const urlTree = router.createUrlTree(['/admin/dashboard']);
          router.navigateByUrl(urlTree);
          return false; // Prevent access to the empty route
        }
        return true;
      }
    )
  );

};

export const canActivateAdminPages: CanActivateChildFn = (childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = localStorage.getItem("token");
  if (!token) {
    return router.createUrlTree(["/", "login"]);
  }

  return authService.getUserDetails(token).pipe(
    map(
      user => {
        const role = user['role'];

        if (role === 0) {
          return true; // Prevent access to the empty route
        } else if (role > 0) {
          const urlTree = router.createUrlTree(['/user/homepage']);
          router.navigateByUrl(urlTree);
          return false; // Prevent access to the empty route
        }

        return true;
      }));
};
