import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { FormService } from '../../core/services/form.service';
import { AuthService } from '../../core/services/auth.service';
import { FormDefinition } from '../../core/models/form.model';

@Component({
  selector: 'app-form-list',
  standalone: true,
  imports: [CommonModule, RouterLink, HeaderComponent],
  template: `
    <app-header title="Forms">
      @if (auth.hasPermission('form.add')) {
        <a routerLink="/forms/new" class="btn btn-primary">Create Form</a>
      }
    </app-header>
    <div class="page-content">
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Status</th>
              <th>Version</th>
              <th>Fields</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            @for (form of forms(); track form.id) {
              <tr>
                <td>{{ form.name }}</td>
                <td>
                  <span [class]="'badge badge-' + form.status.toLowerCase()">
                    {{ form.status }}
                  </span>
                </td>
                <td>v{{ form.version }}</td>
                <td>{{ form.schema?.fields?.length || 0 }}</td>
                <td class="actions">
                  <a [routerLink]="['/forms', form.id]" class="btn btn-sm">View</a>
                  @if (form.status === 'DRAFT' && auth.hasPermission('form.change')) {
                    <button class="btn btn-sm btn-primary" (click)="onPublish(form)">
                      Publish
                    </button>
                  }
                  @if (form.status === 'PUBLISHED' && auth.hasPermission('form.change')) {
                    <button class="btn btn-sm btn-secondary" (click)="onArchive(form)">
                      Archive
                    </button>
                  }
                </td>
              </tr>
            }
            @if (forms().length === 0) {
              <tr>
                <td colspan="5" class="empty-state">No forms found</td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>
  `,
  styles: [
    `
      .page-content {
        padding: 24px 32px;
      }
      .badge-draft {
        background: #374151;
        color: #9ca3af;
      }
      .badge-published {
        background: #065f46;
        color: #6ee7b7;
      }
      .badge-archived {
        background: #78350f;
        color: #fcd34d;
      }
    `,
  ],
})
export class FormListComponent implements OnInit {
  private formService = inject(FormService);
  auth = inject(AuthService);

  forms = signal<FormDefinition[]>([]);

  ngOnInit() {
    this.loadForms();
  }

  loadForms() {
    this.formService.list().subscribe((forms) => this.forms.set(forms));
  }

  onPublish(form: FormDefinition) {
    this.formService.publish(form.id).subscribe(() => this.loadForms());
  }

  onArchive(form: FormDefinition) {
    this.formService.archive(form.id).subscribe(() => this.loadForms());
  }
}
