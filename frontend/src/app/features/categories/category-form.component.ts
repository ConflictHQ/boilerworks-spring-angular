import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { CategoryService } from '../../core/services/category.service';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  template: `
    <app-header [title]="isEdit ? 'Edit Category' : 'New Category'"></app-header>
    <div class="page-content">
      <form class="entity-form" (ngSubmit)="onSubmit()">
        @if (error()) {
          <div class="error-message">{{ error() }}</div>
        }
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" id="name" [(ngModel)]="name" name="name" required class="form-control" />
        </div>
        <div class="form-group">
          <label for="description">Description</label>
          <textarea id="description" [(ngModel)]="description" name="description" rows="3" class="form-control"></textarea>
        </div>
        <div class="form-group">
          <label for="sortOrder">Sort Order</label>
          <input type="number" id="sortOrder" [(ngModel)]="sortOrder" name="sortOrder" class="form-control" />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">{{ isEdit ? 'Update' : 'Create' }}</button>
          <button type="button" class="btn btn-secondary" (click)="onCancel()">Cancel</button>
        </div>
      </form>
    </div>
  `,
  styles: [
    `
      .page-content {
        padding: 24px 32px;
        max-width: 640px;
      }
    `,
  ],
})
export class CategoryFormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private categoryService = inject(CategoryService);

  isEdit = false;
  categoryId = '';
  name = '';
  description = '';
  sortOrder = 0;
  error = signal('');

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.categoryId = id;
      this.categoryService.get(id).subscribe((cat) => {
        this.name = cat.name;
        this.description = cat.description || '';
        this.sortOrder = cat.sortOrder;
      });
    }
  }

  onSubmit() {
    const data = {
      name: this.name,
      description: this.description,
      sortOrder: this.sortOrder,
    };

    const obs = this.isEdit
      ? this.categoryService.update(this.categoryId, data)
      : this.categoryService.create(data);

    obs.subscribe({
      next: (response) => {
        if (response.ok) {
          this.router.navigate(['/categories']);
        } else {
          this.error.set(response.errors?.[0]?.messages?.[0] ?? 'An error occurred');
        }
      },
      error: () => this.error.set('An error occurred'),
    });
  }

  onCancel() {
    this.router.navigate(['/categories']);
  }
}
