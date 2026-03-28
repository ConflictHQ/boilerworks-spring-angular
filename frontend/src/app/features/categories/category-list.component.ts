import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { CategoryService } from '../../core/services/category.service';
import { AuthService } from '../../core/services/auth.service';
import { Category } from '../../core/models/category.model';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, HeaderComponent],
  template: `
    <app-header title="Categories">
      @if (auth.hasPermission('category.add')) {
        <a routerLink="/categories/new" class="btn btn-primary">Add Category</a>
      }
    </app-header>
    <div class="page-content">
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Slug</th>
              <th>Description</th>
              <th>Sort Order</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            @for (cat of categories(); track cat.id) {
              <tr>
                <td>{{ cat.name }}</td>
                <td><code>{{ cat.slug }}</code></td>
                <td>{{ cat.description || '-' }}</td>
                <td>{{ cat.sortOrder }}</td>
                <td class="actions">
                  @if (auth.hasPermission('category.change')) {
                    <a [routerLink]="['/categories', cat.id, 'edit']" class="btn btn-sm">Edit</a>
                  }
                  @if (auth.hasPermission('category.delete')) {
                    <button class="btn btn-sm btn-danger" (click)="onDelete(cat)">Delete</button>
                  }
                </td>
              </tr>
            }
            @if (categories().length === 0) {
              <tr>
                <td colspan="5" class="empty-state">No categories found</td>
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
    `,
  ],
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(CategoryService);
  auth = inject(AuthService);

  categories = signal<Category[]>([]);

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.list().subscribe((cats) => this.categories.set(cats));
  }

  onDelete(category: Category) {
    if (confirm(`Delete "${category.name}"?`)) {
      this.categoryService.delete(category.id).subscribe(() => {
        this.loadCategories();
      });
    }
  }
}
