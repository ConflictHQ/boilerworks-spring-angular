import { TestBed } from '@angular/core/testing';
import { UrlTree } from '@angular/router';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { authGuard, permissionGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';
import { environment } from '../../../environments/environment';

describe('authGuard', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should redirect to login when not authenticated', () => {
    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(result).toBeInstanceOf(UrlTree);
    expect((result as UrlTree).toString()).toBe('/login');
  });

  it('should allow access when authenticated', () => {
    // Set up authenticated user
    authService.fetchCurrentUser().subscribe();
    httpMock
      .expectOne(`${environment.apiUrl}/api/auth/me`)
      .flush({
        ok: true,
        data: {
          id: '1',
          email: 'a@b.com',
          firstName: 'A',
          lastName: 'B',
          active: true,
          staff: false,
          groups: [],
          permissions: [],
        },
      });

    const result = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(result).toBeTrue();
  });
});

describe('permissionGuard', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should redirect to login when not authenticated', () => {
    const guard = permissionGuard('product.view');
    const result = TestBed.runInInjectionContext(() => guard({} as any, {} as any));
    expect(result).toBeInstanceOf(UrlTree);
    expect((result as UrlTree).toString()).toBe('/login');
  });

  it('should allow access when user has required permission', () => {
    authService.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({
      ok: true,
      data: {
        id: '1',
        email: 'a@b.com',
        firstName: 'A',
        lastName: 'B',
        active: true,
        staff: false,
        groups: [],
        permissions: ['product.view', 'product.change'],
      },
    });

    const guard = permissionGuard('product.view');
    const result = TestBed.runInInjectionContext(() => guard({} as any, {} as any));
    expect(result).toBeTrue();
  });

  it('should redirect to dashboard when user lacks permission', () => {
    authService.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({
      ok: true,
      data: {
        id: '1',
        email: 'a@b.com',
        firstName: 'A',
        lastName: 'B',
        active: true,
        staff: false,
        groups: [],
        permissions: ['category.view'],
      },
    });

    const guard = permissionGuard('product.view');
    const result = TestBed.runInInjectionContext(() => guard({} as any, {} as any));
    expect(result).toBeInstanceOf(UrlTree);
    expect((result as UrlTree).toString()).toBe('/dashboard');
  });
});
