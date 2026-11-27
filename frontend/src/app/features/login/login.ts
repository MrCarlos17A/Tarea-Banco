import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { IconComponent } from '../../shared/components/icon';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, IconComponent],
  templateUrl: './login.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent {
  protected readonly auth = inject(AuthService);

  email = 'admin@demo.pe';
  password = '';
  readonly showPassword = signal(false);
  readonly submitting = signal(false);
  readonly errorMessage = signal('');

  togglePassword(): void {
    this.showPassword.update((v) => !v);
  }

  onSubmit(): void {
    if (!this.email.trim() || !this.password) {
      this.errorMessage.set('Ingresa tu correo y contraseña para continuar.');
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set('');
    this.auth.clearNotice();

    this.auth.login(this.email, this.password).subscribe({
      next: () => {
        this.password = '';
        this.submitting.set(false);
      },
      error: (err: unknown) => {
        this.submitting.set(false);
        if (err instanceof HttpErrorResponse && err.status === 401) {
          this.errorMessage.set('Correo o contraseña incorrectos.');
        } else {
          this.errorMessage.set('No pudimos conectarnos con el servicio. Intenta nuevamente.');
        }
      }
    });
  }
}
