import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ProductService } from '../../core/services/product.service';
import { CategoryService } from '../../core/services/category.service';
import { Category } from '../../core/models/category.model';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule, HeaderComponent],
  template: `
    <app-header [title]="isEdit ? 'Edit Product' : 'New Product'"></app-header>
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
        <div class="form-row">
          <div class="form-group">
            <label for="price">Price</label>
            <input type="number" id="price" [(ngModel)]="price" name="price" required step="0.01" min="0" class="form-control" />
          </div>
          <div class="form-group">
            <label for="sku">SKU</label>
            <input type="text" id="sku" [(ngModel)]="sku" name="sku" required class="form-control" />
          </div>
        </div>
        <div class="form-group">
          <label for="categoryId">Category</label>
          <select id="categoryId" [(ngModel)]="categoryId" name="categoryId" class="form-control">
            <option value="">None</option>
            @for (cat of categories(); track cat.id) {
              <option [value]="cat.id">{{ cat.name }}</option>
            }
          </select>
        </div>
        <div class="form-group">
          <label>
            <input type="checkbox" [(ngModel)]="active" name="active" />
            Active
          </label>
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
      .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
      }
    `,
  ],
})
export class ProductFormComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);

  isEdit = false;
  productId = '';
  name = '';
  description = '';
  price = 0;
  sku = '';
  active = true;
  categoryId = '';
  categories = signal<Category[]>([]);
  error = signal('');

  ngOnInit() {
    this.categoryService.list().subscribe((cats) => this.categories.set(cats));

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.productId = id;
      this.productService.get(id).subscribe((product) => {
        this.name = product.name;
        this.description = product.description || '';
        this.price = product.price;
        this.sku = product.sku;
        this.active = product.active;
        this.categoryId = product.categoryId || '';
      });
    }
  }

  onSubmit() {
    const data = {
      name: this.name,
      description: this.description,
      price: this.price,
      sku: this.sku,
      active: this.active,
      categoryId: this.categoryId || undefined,
    };

    const obs = this.isEdit
      ? this.productService.update(this.productId, data)
      : this.productService.create(data);

    obs.subscribe({
      next: (response) => {
        if (response.ok) {
          this.router.navigate(['/products']);
        } else {
          this.error.set(response.errors?.[0]?.messages?.[0] ?? 'An error occurred');
        }
      },
      error: () => this.error.set('An error occurred'),
    });
  }

  onCancel() {
    this.router.navigate(['/products']);
  }
}
