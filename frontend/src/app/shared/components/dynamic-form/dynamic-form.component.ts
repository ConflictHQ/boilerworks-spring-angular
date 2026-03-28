import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormField } from '../../../core/models/form.model';

@Component({
  selector: 'app-dynamic-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <form class="dynamic-form" (ngSubmit)="onSubmit()">
      @for (field of fields; track field.name) {
        <div class="form-group">
          <label [for]="field.name">
            {{ field.label }}
            @if (field.required) {
              <span class="required">*</span>
            }
          </label>

          @switch (field.type) {
            @case ('textarea') {
              <textarea
                [id]="field.name"
                [name]="field.name"
                [(ngModel)]="formData[field.name]"
                [required]="field.required ?? false"
                rows="4"
                class="form-control"
              ></textarea>
            }
            @case ('select') {
              <select
                [id]="field.name"
                [name]="field.name"
                [(ngModel)]="formData[field.name]"
                [required]="field.required ?? false"
                class="form-control"
              >
                <option value="">Select...</option>
                @for (option of field.options; track option) {
                  <option [value]="option">{{ option }}</option>
                }
              </select>
            }
            @case ('checkbox') {
              <input
                type="checkbox"
                [id]="field.name"
                [name]="field.name"
                [(ngModel)]="formData[field.name]"
                class="form-checkbox"
              />
            }
            @case ('number') {
              <input
                type="number"
                [id]="field.name"
                [name]="field.name"
                [(ngModel)]="formData[field.name]"
                [required]="field.required ?? false"
                [min]="field.min ?? null"
                [max]="field.max ?? null"
                class="form-control"
              />
            }
            @default {
              <input
                [type]="field.type"
                [id]="field.name"
                [name]="field.name"
                [(ngModel)]="formData[field.name]"
                [required]="field.required ?? false"
                class="form-control"
              />
            }
          }

          @if (errors[field.name]) {
            <div class="field-error">{{ errors[field.name] }}</div>
          }
        </div>
      }

      <div class="form-actions">
        <button type="submit" class="btn btn-primary">{{ submitLabel }}</button>
        @if (showCancel) {
          <button type="button" class="btn btn-secondary" (click)="onCancel()">Cancel</button>
        }
      </div>
    </form>
  `,
  styles: [
    `
      .dynamic-form {
        display: flex;
        flex-direction: column;
        gap: 16px;
      }
      .form-group {
        display: flex;
        flex-direction: column;
        gap: 6px;
      }
      .form-group label {
        color: #c4c9d4;
        font-size: 14px;
        font-weight: 500;
      }
      .required {
        color: #ef4444;
      }
      .form-control {
        padding: 10px 12px;
        background: #1a1d23;
        border: 1px solid #374151;
        border-radius: 6px;
        color: #e2e8f0;
        font-size: 14px;
      }
      .form-control:focus {
        outline: none;
        border-color: #2563eb;
      }
      .form-checkbox {
        width: 18px;
        height: 18px;
      }
      .field-error {
        color: #ef4444;
        font-size: 12px;
      }
      .form-actions {
        display: flex;
        gap: 12px;
        padding-top: 8px;
      }
    `,
  ],
})
export class DynamicFormComponent implements OnChanges {
  @Input() fields: FormField[] = [];
  @Input() initialData: Record<string, any> = {};
  @Input() submitLabel = 'Submit';
  @Input() showCancel = false;
  @Input() errors: Record<string, string> = {};

  @Output() formSubmit = new EventEmitter<Record<string, any>>();
  @Output() formCancel = new EventEmitter<void>();

  formData: Record<string, any> = {};

  ngOnChanges(changes: SimpleChanges) {
    if (changes['initialData'] || changes['fields']) {
      this.formData = { ...this.initialData };
    }
  }

  onSubmit() {
    this.formSubmit.emit({ ...this.formData });
  }

  onCancel() {
    this.formCancel.emit();
  }
}
