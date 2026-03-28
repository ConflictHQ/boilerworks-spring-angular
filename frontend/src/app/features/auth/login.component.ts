import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="login-page">
      <div class="login-card">
        <div class="login-header">
          <h1>Boilerworks</h1>
          <p>Sign in to your account</p>
        </div>
        <form (ngSubmit)="onLogin()">
          @if (error) {
            <div class="error-message">{{ error }}</div>
          }
          <div class="form-group">
            <label for="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              [(ngModel)]="email"
              required
              class="form-control"
              placeholder="admin@boilerworks.dev"
            />
          </div>
          <div class="form-group">
            <label for="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              [(ngModel)]="password"
              required
              class="form-control"
              placeholder="Enter your password"
            />
          </div>
          <button type="submit" class="btn btn-primary" [disabled]="loading">
            {{ loading ? 'Signing in...' : 'Sign in' }}
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [
    `
      .login-page {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #0f1117;
      }
      .login-card {
        width: 400px;
        padding: 40px;
        background: #1a1d23;
        border-radius: 12px;
        border: 1px solid #2d3139;
      }
      .login-header {
        text-align: center;
        margin-bottom: 32px;
      }
      .login-header h1 {
        color: #e2e8f0;
        font-size: 28px;
        margin: 0 0 8px;
      }
      .login-header p {
        color: #6b7280;
        margin: 0;
      }
      .form-group {
        margin-bottom: 20px;
      }
      .form-group label {
        display: block;
        color: #c4c9d4;
        font-size: 14px;
        font-weight: 500;
        margin-bottom: 6px;
      }
      .form-control {
        width: 100%;
        padding: 10px 12px;
        background: #22262e;
        border: 1px solid #374151;
        border-radius: 6px;
        color: #e2e8f0;
        font-size: 14px;
        box-sizing: border-box;
      }
      .form-control:focus {
        outline: none;
        border-color: #2563eb;
      }
      .btn-primary {
        width: 100%;
        padding: 12px;
        background: #2563eb;
        border: none;
        border-radius: 6px;
        color: #fff;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: background 0.15s;
      }
      .btn-primary:hover {
        background: #1d4ed8;
      }
      .btn-primary:disabled {
        opacity: 0.6;
        cursor: not-allowed;
      }
      .error-message {
        background: #7f1d1d;
        color: #fca5a5;
        padding: 10px 12px;
        border-radius: 6px;
        font-size: 13px;
        margin-bottom: 16px;
      }
    `,
  ],
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  email = '';
  password = '';
  error = '';
  loading = false;

  onLogin() {
    this.loading = true;
    this.error = '';

    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.ok) {
          this.router.navigate(['/dashboard']);
        } else {
          this.error = response.errors?.[0]?.messages?.[0] ?? 'Login failed';
        }
      },
      error: () => {
        this.loading = false;
        this.error = 'Invalid email or password';
      },
    });
  }
}
