import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ProductService } from '../../core/services/product.service';
import { AuthService } from '../../core/services/auth.service';
import { Product } from '../../core/models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, HeaderComponent],
  template: `
    <app-header title="Products">
      @if (auth.hasPermission('product.add')) {
        <a routerLink="/products/new" class="btn btn-primary">Add Product</a>
      }
    </app-header>
    <div class="page-content">
      <div class="search-bar">
        <input
          type="text"
          [(ngModel)]="searchTerm"
          (ngModelChange)="onSearch($event)"
          placeholder="Search products..."
          class="form-control"
        />
      </div>
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>SKU</th>
              <th>Price</th>
              <th>Category</th>
              <th>Active</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            @for (product of products(); track product.id) {
              <tr>
                <td>{{ product.name }}</td>
                <td><code>{{ product.sku }}</code></td>
                <td>{{ product.price | currency }}</td>
                <td>{{ product.categoryName || '-' }}</td>
                <td>
                  <span [class]="product.active ? 'badge badge-success' : 'badge badge-muted'">
                    {{ product.active ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="actions">
                  @if (auth.hasPermission('product.change')) {
                    <a [routerLink]="['/products', product.id, 'edit']" class="btn btn-sm">Edit</a>
                  }
                  @if (auth.hasPermission('product.delete')) {
                    <button class="btn btn-sm btn-danger" (click)="onDelete(product)">Delete</button>
                  }
                </td>
              </tr>
            }
            @if (products().length === 0) {
              <tr>
                <td colspan="6" class="empty-state">No products found</td>
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
      .search-bar {
        margin-bottom: 20px;
        max-width: 400px;
      }
    `,
  ],
})
export class ProductListComponent implements OnInit {
  private productService = inject(ProductService);
  auth = inject(AuthService);

  products = signal<Product[]>([]);
  searchTerm = '';

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.productService.list(this.searchTerm || undefined).subscribe((products) => {
      this.products.set(products);
    });
  }

  onSearch(term: string) {
    this.searchTerm = term;
    this.loadProducts();
  }

  onDelete(product: Product) {
    if (confirm(`Delete "${product.name}"?`)) {
      this.productService.delete(product.id).subscribe(() => {
        this.loadProducts();
      });
    }
  }
}
