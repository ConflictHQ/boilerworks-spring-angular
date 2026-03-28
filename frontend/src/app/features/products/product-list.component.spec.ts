import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { ProductListComponent } from './product-list.component';
import { environment } from '../../../environments/environment';
import { Product } from '../../core/models/product.model';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let httpMock: HttpTestingController;

  const mockProducts: Product[] = [
    {
      id: '1',
      name: 'Widget',
      slug: 'widget',
      description: 'A widget',
      price: 9.99,
      sku: 'WDG-001',
      active: true,
      categoryId: 'c1',
      categoryName: 'Electronics',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    },
    {
      id: '2',
      name: 'Gadget',
      slug: 'gadget',
      description: 'A gadget',
      price: 19.99,
      sku: 'GDG-001',
      active: false,
      categoryId: 'c2',
      categoryName: 'Tools',
      createdAt: '2024-01-01',
      updatedAt: '2024-01-01',
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductListComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/products`).flush([]);
    expect(component).toBeTruthy();
  });

  it('should render product rows after loading', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/products`).flush(mockProducts);
    fixture.detectChanges();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toContain('Widget');
    expect(rows[0].textContent).toContain('WDG-001');
    expect(rows[1].textContent).toContain('Gadget');
  });

  it('should show empty state when no products', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/products`).flush([]);
    fixture.detectChanges();

    const emptyCell = fixture.nativeElement.querySelector('.empty-state');
    expect(emptyCell).toBeTruthy();
    expect(emptyCell.textContent).toContain('No products found');
  });

  it('should display active/inactive badges', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/products`).flush(mockProducts);
    fixture.detectChanges();

    const badges = fixture.nativeElement.querySelectorAll('.badge');
    expect(badges[0].textContent.trim()).toBe('Active');
    expect(badges[0].classList).toContain('badge-success');
    expect(badges[1].textContent.trim()).toBe('Inactive');
    expect(badges[1].classList).toContain('badge-muted');
  });
});
