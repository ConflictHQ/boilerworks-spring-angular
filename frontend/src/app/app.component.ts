import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent],
  template: `
    @if (auth.isAuthenticated() && !isLoginPage()) {
      <div class="app-layout">
        <app-sidebar></app-sidebar>
        <main class="main-content">
          <router-outlet></router-outlet>
        </main>
      </div>
    } @else {
      <router-outlet></router-outlet>
    }
  `,
  styles: [
    `
      .app-layout {
        display: flex;
        min-height: 100vh;
      }
      .main-content {
        flex: 1;
        background: #14161b;
        overflow-y: auto;
      }
    `,
  ],
})
export class AppComponent implements OnInit {
  auth = inject(AuthService);
  private router = inject(Router);

  ngOnInit() {
    this.auth.fetchCurrentUser().subscribe();
  }

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}
