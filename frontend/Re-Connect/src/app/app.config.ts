import "./shared/extensions/form.extension";

import { ApplicationConfig } from '@angular/core';
import { provideAnimationsAsync } from "@angular/platform-browser/animations/async";
import { provideRouter, withHashLocation } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { customInterceptor } from './shared/components/auth/custom.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [provideAnimationsAsync(), provideRouter(routes, withHashLocation()), provideHttpClient(withInterceptors([customInterceptor]))]
};
