import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

interface NavItem {
  label: string;
  path: string;
  icon: string;
  permission?: string;
}

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <aside class="sidebar">
      <div class="sidebar-brand">
        <h2>Boilerworks</h2>
        <span class="sidebar-subtitle">Admin</span>
      </div>
      <nav class="sidebar-nav">
        @for (item of visibleNavItems(); track item.path) {
          <a
            [routerLink]="item.path"
            routerLinkActive="active"
            class="nav-item"
          >
            <span class="nav-icon">{{ item.icon }}</span>
            <span class="nav-label">{{ item.label }}</span>
          </a>
        }
      </nav>
      <div class="sidebar-footer">
        <div class="user-info">
          <span class="user-name">{{ auth.user()?.firstName }} {{ auth.user()?.lastName }}</span>
          <span class="user-email">{{ auth.user()?.email }}</span>
        </div>
        <button class="logout-btn" (click)="onLogout()">Logout</button>
      </div>
    </aside>
  `,
  styles: [
    `
      .sidebar {
        width: 260px;
        min-height: 100vh;
        background: #1a1d23;
        border-right: 1px solid #2d3139;
        display: flex;
        flex-direction: column;
        color: #c4c9d4;
      }
      .sidebar-brand {
        padding: 24px 20px;
        border-bottom: 1px solid #2d3139;
      }
      .sidebar-brand h2 {
        margin: 0;
        color: #e2e8f0;
        font-size: 20px;
        font-weight: 700;
      }
      .sidebar-subtitle {
        font-size: 12px;
        color: #6b7280;
        text-transform: uppercase;
        letter-spacing: 1px;
      }
      .sidebar-nav {
        flex: 1;
        padding: 16px 12px;
        display: flex;
        flex-direction: column;
        gap: 4px;
      }
      .nav-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 10px 12px;
        border-radius: 8px;
        color: #9ca3af;
        text-decoration: none;
        font-size: 14px;
        transition: all 0.15s;
      }
      .nav-item:hover {
        background: #22262e;
        color: #e2e8f0;
      }
      .nav-item.active {
        background: #2563eb;
        color: #fff;
      }
      .nav-icon {
        font-size: 18px;
        width: 24px;
        text-align: center;
      }
      .sidebar-footer {
        padding: 16px 20px;
        border-top: 1px solid #2d3139;
      }
      .user-info {
        display: flex;
        flex-direction: column;
        margin-bottom: 12px;
      }
      .user-name {
        color: #e2e8f0;
        font-size: 14px;
        font-weight: 500;
      }
      .user-email {
        color: #6b7280;
        font-size: 12px;
      }
      .logout-btn {
        width: 100%;
        padding: 8px;
        background: transparent;
        border: 1px solid #374151;
        border-radius: 6px;
        color: #9ca3af;
        cursor: pointer;
        font-size: 13px;
        transition: all 0.15s;
      }
      .logout-btn:hover {
        background: #374151;
        color: #e2e8f0;
      }
    `,
  ],
})
export class SidebarComponent {
  auth = inject(AuthService);

  private navItems: NavItem[] = [
    { label: 'Dashboard', path: '/dashboard', icon: '\u2302' },
    { label: 'Products', path: '/products', icon: '\u2756', permission: 'product.view' },
    { label: 'Categories', path: '/categories', icon: '\u2630', permission: 'category.view' },
    { label: 'Forms', path: '/forms', icon: '\u2611', permission: 'form.view' },
    { label: 'Workflows', path: '/workflows', icon: '\u21C4', permission: 'workflow.view' },
  ];

  visibleNavItems() {
    return this.navItems.filter(
      (item) => !item.permission || this.auth.hasPermission(item.permission)
    );
  }

  onLogout() {
    this.auth.logout().subscribe();
  }
}
