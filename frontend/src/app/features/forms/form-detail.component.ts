import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { DynamicFormComponent } from '../../shared/components/dynamic-form/dynamic-form.component';
import { FormService } from '../../core/services/form.service';
import { AuthService } from '../../core/services/auth.service';
import { FormDefinition, FormSubmission } from '../../core/models/form.model';

@Component({
  selector: 'app-form-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, HeaderComponent, DynamicFormComponent],
  template: `
    <app-header [title]="form()?.name ?? 'Form'">
      <a routerLink="/forms" class="btn btn-secondary">Back</a>
    </app-header>
    <div class="page-content">
      @if (form(); as f) {
        <div class="form-info">
          <span [class]="'badge badge-' + f.status.toLowerCase()">{{ f.status }}</span>
          <span class="version">v{{ f.version }}</span>
          @if (f.description) {
            <p>{{ f.description }}</p>
          }
        </div>

        @if (f.status === 'PUBLISHED' && auth.hasPermission('form.submit')) {
          <div class="form-section">
            <h3>Submit Response</h3>
            @if (submitMessage()) {
              <div [class]="submitError() ? 'error-message' : 'success-message'">
                {{ submitMessage() }}
              </div>
            }
            <app-dynamic-form
              [fields]="f.schema.fields"
              submitLabel="Submit"
              (formSubmit)="onFormSubmit($event)"
            ></app-dynamic-form>
          </div>
        }

        <div class="form-section">
          <h3>Submissions ({{ submissions().length }})</h3>
          @for (sub of submissions(); track sub.id) {
            <div class="submission-card">
              <div class="submission-header">
                <span [class]="sub.valid ? 'badge badge-success' : 'badge badge-danger'">
                  {{ sub.valid ? 'Valid' : 'Invalid' }}
                </span>
                <span class="submission-date">{{ sub.createdAt | date:'medium' }}</span>
              </div>
              <pre>{{ sub.data | json }}</pre>
            </div>
          }
        </div>
      }
    </div>
  `,
  styles: [
    `
      .page-content {
        padding: 24px 32px;
        max-width: 800px;
      }
      .form-info {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 24px;
      }
      .form-info p {
        color: #9ca3af;
        margin: 0;
      }
      .version {
        color: #6b7280;
        font-size: 13px;
      }
      .form-section {
        margin-top: 32px;
      }
      .form-section h3 {
        color: #e2e8f0;
        margin: 0 0 16px;
      }
      .submission-card {
        background: #1a1d23;
        border: 1px solid #2d3139;
        border-radius: 8px;
        padding: 16px;
        margin-bottom: 12px;
      }
      .submission-header {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 12px;
      }
      .submission-date {
        color: #6b7280;
        font-size: 12px;
      }
      pre {
        background: #22262e;
        padding: 12px;
        border-radius: 6px;
        color: #c4c9d4;
        font-size: 12px;
        overflow-x: auto;
      }
      .success-message {
        background: #065f46;
        color: #6ee7b7;
        padding: 10px 12px;
        border-radius: 6px;
        font-size: 13px;
        margin-bottom: 16px;
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
export class FormDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private formService = inject(FormService);
  auth = inject(AuthService);

  form = signal<FormDefinition | null>(null);
  submissions = signal<FormSubmission[]>([]);
  submitMessage = signal('');
  submitError = signal(false);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.formService.get(id).subscribe((form) => this.form.set(form));
      this.formService.getSubmissions(id).subscribe((subs) => this.submissions.set(subs));
    }
  }

  onFormSubmit(data: Record<string, any>) {
    const id = this.form()?.id;
    if (!id) return;

    this.formService.submit(id, data).subscribe({
      next: (response) => {
        if (response.ok) {
          this.submitMessage.set(response.data.valid ? 'Submitted successfully' : 'Submitted with validation errors');
          this.submitError.set(!response.data.valid);
          this.formService.getSubmissions(id).subscribe((subs) => this.submissions.set(subs));
        }
      },
      error: () => {
        this.submitMessage.set('Submission failed');
        this.submitError.set(true);
      },
    });
  }
}
