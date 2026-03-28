import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <header class="page-header">
      <h1>{{ title }}</h1>
      <ng-content></ng-content>
    </header>
  `,
  styles: [
    `
      .page-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 20px 32px;
        border-bottom: 1px solid #2d3139;
        background: #1e2128;
      }
      .page-header h1 {
        margin: 0;
        color: #e2e8f0;
        font-size: 24px;
        font-weight: 600;
      }
    `,
  ],
})
export class HeaderComponent {
  @Input() title = '';
}
