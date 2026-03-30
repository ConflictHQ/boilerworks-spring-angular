import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, HeaderComponent],
  template: `
    <app-header title="Dashboard"></app-header>
    <div class="dashboard-content">
      <div class="welcome">
        <h2>Welcome, {{ auth.user()?.firstName }}</h2>
        <p>Boilerworks Spring Boot + Angular admin panel.</p>
      </div>
      <div class="dashboard-grid">
        @if (auth.hasPermission('item.view')) {
          <a routerLink="/items" class="dashboard-card">
            <div class="card-icon">❖</div>
            <h3>Items</h3>
            <p>Manage item catalogue</p>
          </a>
        }
        @if (auth.hasPermission('category.view')) {
          <a routerLink="/categories" class="dashboard-card">
            <div class="card-icon">☰</div>
            <h3>Categories</h3>
            <p>Organize item categories</p>
          </a>
        }
        @if (auth.hasPermission('form.view')) {
          <a routerLink="/forms" class="dashboard-card">
            <div class="card-icon">☑</div>
            <h3>Forms</h3>
            <p>Dynamic form builder</p>
          </a>
        }
        @if (auth.hasPermission('workflow.view')) {
          <a routerLink="/workflows" class="dashboard-card">
            <div class="card-icon">⇄</div>
            <h3>Workflows</h3>
            <p>State machine workflows</p>
          </a>
        }
      </div>
    </div>
  `,
  styles: [
    `
      .dashboard-content {
        padding: 32px;
      }
      .welcome {
        margin-bottom: 32px;
      }
      .welcome h2 {
        color: #e2e8f0;
        font-size: 20px;
        margin: 0 0 8px;
      }
      .welcome p {
        color: #6b7280;
        margin: 0;
      }
      .dashboard-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
        gap: 20px;
      }
      .dashboard-card {
        padding: 24px;
        background: #1a1d23;
        border: 1px solid #2d3139;
        border-radius: 10px;
        text-decoration: none;
        transition: all 0.15s;
      }
      .dashboard-card:hover {
        border-color: #2563eb;
        transform: translateY(-2px);
      }
      .card-icon {
        font-size: 28px;
        margin-bottom: 12px;
      }
      .dashboard-card h3 {
        color: #e2e8f0;
        margin: 0 0 6px;
        font-size: 16px;
      }
      .dashboard-card p {
        color: #6b7280;
        margin: 0;
        font-size: 13px;
      }
    `,
  ],
})
export class DashboardComponent {
  auth = inject(AuthService);
}
