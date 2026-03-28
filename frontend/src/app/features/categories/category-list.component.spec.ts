import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { CategoryListComponent } from './category-list.component';
import { environment } from '../../../environments/environment';
import { Category } from '../../core/models/category.model';

describe('CategoryListComponent', () => {
  let component: CategoryListComponent;
  let fixture: ComponentFixture<CategoryListComponent>;
  let httpMock: HttpTestingController;

  const mockCategories: Category[] = [
    {
      id: 'c1',
      name: 'Electronics',
      slug: 'electronics',
      description: 'Electronic devices',
      sortOrder: 1,
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    },
    {
      id: 'c2',
      name: 'Tools',
      slug: 'tools',
      description: '',
      sortOrder: 2,
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoryListComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);
    expect(component).toBeTruthy();
  });

  it('should render category rows', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush(mockCategories);
    fixture.detectChanges();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toContain('Electronics');
    expect(rows[0].textContent).toContain('electronics');
    expect(rows[1].textContent).toContain('Tools');
  });

  it('should show empty state when no categories', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);
    fixture.detectChanges();

    const emptyCell = fixture.nativeElement.querySelector('.empty-state');
    expect(emptyCell).toBeTruthy();
    expect(emptyCell.textContent).toContain('No categories found');
  });

  it('should show dash for empty description', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([mockCategories[1]]);
    fixture.detectChanges();

    const cells = fixture.nativeElement.querySelectorAll('tbody td');
    // description column is the 3rd (index 2)
    expect(cells[2].textContent.trim()).toBe('-');
  });
});
