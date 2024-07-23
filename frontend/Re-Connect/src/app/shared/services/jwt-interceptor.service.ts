import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { catchError, of, throwError } from "rxjs";
import { ToastService } from "./toast.service";
import { AuthService } from "./auth.service";
import { OverlayService } from "./overlay.service";

export const JWTInterceptor: HttpInterceptorFn = (req, next) => {
    let handled: boolean = false;
    return next(req).pipe(catchError((returnedError) => {
        let errorMessage: any = null;

        if (returnedError.error instanceof ErrorEvent) {
            errorMessage = `Error: ${returnedError.error.message}`;
        } else if (returnedError instanceof HttpErrorResponse) {
            errorMessage = `Error Status ${returnedError.status}: ${returnedError.error.error} - ${returnedError.error.message}`;
            handled = handleServerSideError(returnedError);
        }

        console.error(errorMessage ? errorMessage : returnedError);

        if (!handled) {
            if (errorMessage) {
                return throwError(errorMessage);
            } else {
                return throwError("Unexpected problem occurred");
            }
        } else {
            return of(returnedError);
        }
    }));
}

function handleServerSideError(error: HttpErrorResponse): boolean {
    let handled: boolean = false;
    const toastService = inject(ToastService);
    const authService = inject(AuthService);
    const overlayService = inject(OverlayService);
    switch (error.status) {
        case 401:
            toastService.showError("Please login again");
            authService.logout();
            handled = true;
            break;
        case 403:
            toastService.showError("Please login again");
            authService.logout();
            handled = true;
            break;
        case 500:
            overlayService.hideOverlay();
            toastService.showError("Internal Server Error");
            handled = true;
            break;
    }

    return handled;
}