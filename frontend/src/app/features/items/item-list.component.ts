import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { ItemService } from '../../core/services/item.service';
import { AuthService } from '../../core/services/auth.service';
import { Item } from '../../core/models/item.model';

@Component({
  selector: 'app-item-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, HeaderComponent],
  template: `
    <app-header title="Items">
      @if (auth.hasPermission('item.add')) {
        <a routerLink="/items/new" class="btn btn-primary">Add Item</a>
      }
    </app-header>
    <div class="page-content">
      <div class="search-bar">
        <input
          type="text"
          [(ngModel)]="searchTerm"
          (ngModelChange)="onSearch($event)"
          placeholder="Search items..."
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
            @for (item of items(); track item.id) {
              <tr>
                <td>{{ item.name }}</td>
                <td><code>{{ item.sku }}</code></td>
                <td>{{ item.price | currency }}</td>
                <td>{{ item.categoryName || '-' }}</td>
                <td>
                  <span [class]="item.active ? 'badge badge-success' : 'badge badge-muted'">
                    {{ item.active ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td class="actions">
                  @if (auth.hasPermission('item.change')) {
                    <a [routerLink]="['/items', item.id, 'edit']" class="btn btn-sm">Edit</a>
                  }
                  @if (auth.hasPermission('item.delete')) {
                    <button class="btn btn-sm btn-danger" (click)="onDelete(item)">Delete</button>
                  }
                </td>
              </tr>
            }
            @if (items().length === 0) {
              <tr>
                <td colspan="6" class="empty-state">No items found</td>
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
export class ItemListComponent implements OnInit {
  private itemService = inject(ItemService);
  auth = inject(AuthService);

  items = signal<Item[]>([]);
  searchTerm = '';

  ngOnInit() {
    this.loadItems();
  }

  loadItems() {
    this.itemService.list(this.searchTerm || undefined).subscribe((items) => {
      this.items.set(items);
    });
  }

  onSearch(term: string) {
    this.searchTerm = term;
    this.loadItems();
  }

  onDelete(item: Item) {
    if (confirm(`Delete "${item.name}"?`)) {
      this.itemService.delete(item.id).subscribe(() => {
        this.loadItems();
      });
    }
  }
}
