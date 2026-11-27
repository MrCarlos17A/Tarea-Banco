import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

const SESSION_KEY = 'bcp_portal_session';
const USER_KEY = 'bcp_portal_user';

interface LoginResponse {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly _token = signal<string>(sessionStorage.getItem(SESSION_KEY) ?? '');
  private readonly _userEmail = signal<string>(sessionStorage.getItem(USER_KEY) ?? '');

  readonly token = this._token.asReadonly();
  readonly userEmail = this._userEmail.asReadonly();
  readonly isAuthenticated = computed(() => Boolean(this._token()));
  readonly sessionExpiredNotice = signal<string>('');

  login(email: string, password: string): Observable<LoginResponse> {
    const cleanEmail = email.trim();
    return this.http.post<LoginResponse>('/api/login', { email: cleanEmail, password }).pipe(
      tap((res) => {
        if (res?.token) {
          this._token.set(res.token);
          this._userEmail.set(cleanEmail);
          this.sessionExpiredNotice.set('');
          sessionStorage.setItem(SESSION_KEY, res.token);
          sessionStorage.setItem(USER_KEY, cleanEmail);
        }
      })
    );
  }

  logout(notice = ''): void {
    this._token.set('');
    this._userEmail.set('');
    sessionStorage.removeItem(SESSION_KEY);
    sessionStorage.removeItem(USER_KEY);
    if (notice) {
      this.sessionExpiredNotice.set(notice);
    }
  }

  clearNotice(): void {
    this.sessionExpiredNotice.set('');
  }
}
