import { Routes } from '@angular/router';
import { authGuard, permissionGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
  },
  {
    path: 'items',
    canActivate: [authGuard, permissionGuard('item.view')],
    loadComponent: () =>
      import('./features/items/item-list.component').then((m) => m.ItemListComponent),
  },
  {
    path: 'items/new',
    canActivate: [authGuard, permissionGuard('item.add')],
    loadComponent: () =>
      import('./features/items/item-form.component').then((m) => m.ItemFormComponent),
  },
  {
    path: 'items/:id/edit',
    canActivate: [authGuard, permissionGuard('item.change')],
    loadComponent: () =>
      import('./features/items/item-form.component').then((m) => m.ItemFormComponent),
  },
  {
    path: 'categories',
    canActivate: [authGuard, permissionGuard('category.view')],
    loadComponent: () =>
      import('./features/categories/category-list.component').then((m) => m.CategoryListComponent),
  },
  {
    path: 'categories/new',
    canActivate: [authGuard, permissionGuard('category.add')],
    loadComponent: () =>
      import('./features/categories/category-form.component').then((m) => m.CategoryFormComponent),
  },
  {
    path: 'categories/:id/edit',
    canActivate: [authGuard, permissionGuard('category.change')],
    loadComponent: () =>
      import('./features/categories/category-form.component').then((m) => m.CategoryFormComponent),
  },
  {
    path: 'forms',
    canActivate: [authGuard, permissionGuard('form.view')],
    loadComponent: () =>
      import('./features/forms/form-list.component').then((m) => m.FormListComponent),
  },
  {
    path: 'forms/:id',
    canActivate: [authGuard, permissionGuard('form.view')],
    loadComponent: () =>
      import('./features/forms/form-detail.component').then((m) => m.FormDetailComponent),
  },
  {
    path: 'workflows',
    canActivate: [authGuard, permissionGuard('workflow.view')],
    loadComponent: () =>
      import('./features/workflows/workflow-list.component').then((m) => m.WorkflowListComponent),
  },
  {
    path: 'workflows/:id',
    canActivate: [authGuard, permissionGuard('workflow.view')],
    loadComponent: () =>
      import('./features/workflows/workflow-detail.component').then(
        (m) => m.WorkflowDetailComponent,
      ),
  },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' },
];
