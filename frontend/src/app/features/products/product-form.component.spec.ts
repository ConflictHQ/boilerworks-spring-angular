import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { ProductFormComponent } from './product-form.component';
import { environment } from '../../../environments/environment';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFormComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create in new mode by default', () => {
    fixture.detectChanges();
    // categories list request
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);

    expect(component).toBeTruthy();
    expect(component.isEdit).toBeFalse();
  });

  it('should have empty form fields initially', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);

    expect(component.name).toBe('');
    expect(component.price).toBe(0);
    expect(component.sku).toBe('');
    expect(component.active).toBeTrue();
  });

  it('should submit create request on new product', () => {
    spyOn(router, 'navigate');
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);

    component.name = 'New Product';
    component.price = 29.99;
    component.sku = 'NP-001';
    component.description = 'A new product';
    component.active = true;
    component.categoryId = '';

    component.onSubmit();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/products`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body.name).toBe('New Product');
    expect(req.request.body.price).toBe(29.99);
    req.flush({ ok: true, data: { id: '1', name: 'New Product' } });

    expect(router.navigate).toHaveBeenCalledWith(['/products']);
  });

  it('should display error on failed submission', () => {
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);

    component.name = 'Dupe';
    component.price = 10;
    component.sku = 'DUPE';
    component.onSubmit();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/products`);
    req.flush({
      ok: false,
      data: null,
      errors: [{ field: 'sku', messages: ['SKU already exists'] }],
    });

    expect(component.error()).toBe('SKU already exists');
  });

  it('should navigate back on cancel', () => {
    spyOn(router, 'navigate');
    fixture.detectChanges();
    httpMock.expectOne(`${environment.apiUrl}/api/categories`).flush([]);

    component.onCancel();
    expect(router.navigate).toHaveBeenCalledWith(['/products']);
  });
});
