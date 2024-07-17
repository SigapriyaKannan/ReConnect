import "./shared/extensions/form.extension";

import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from "@angular/platform-browser/animations/async";
import { provideRouter, withHashLocation } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { authInterceptor } from './shared/services/auth-interceptor.service';

export const appConfig: ApplicationConfig = {
  providers: [provideAnimationsAsync(), provideRouter(routes, withHashLocation()), provideHttpClient(withInterceptors([authInterceptor]))]
};
