import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const isApiRequest = req.url.startsWith('/api/');
  const isLoginRequest = req.url === '/api/login';

  const token = auth.token();
  const cloned =
    isApiRequest && !isLoginRequest && token
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req;

  return next(cloned).pipe(
    catchError((err: unknown) => {
      if (err instanceof HttpErrorResponse && err.status === 401 && !isLoginRequest) {
        auth.logout('Tu sesión ha finalizado. Por favor, vuelve a ingresar.');
      }
      return throwError(() => err);
    })
  );
};
