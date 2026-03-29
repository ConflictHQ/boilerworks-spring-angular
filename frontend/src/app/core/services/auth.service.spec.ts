import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start unauthenticated', () => {
    expect(service.isAuthenticated()).toBeFalse();
    expect(service.user()).toBeNull();
    expect(service.permissions()).toEqual([]);
  });

  it('should set user on successful login', () => {
    const mockUser = {
      id: '1',
      email: 'test@example.com',
      firstName: 'Test',
      lastName: 'User',
      active: true,
      staff: false,
      groups: [],
      permissions: ['item.view'],
    };

    service.login('test@example.com', 'password').subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/auth/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'test@example.com', password: 'password' });
    expect(req.request.withCredentials).toBeTrue();
    req.flush({ ok: true, data: mockUser });

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.user()?.email).toBe('test@example.com');
    expect(service.permissions()).toEqual(['item.view']);
  });

  it('should not set user on failed login', () => {
    service.login('test@example.com', 'wrong').subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/auth/login`);
    req.flush({ ok: false, data: null, errors: [{ field: 'email', messages: ['Invalid'] }] });

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.user()).toBeNull();
  });

  it('should clear user and navigate on logout', () => {
    spyOn(router, 'navigate');

    service.logout().subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/auth/logout`);
    expect(req.request.method).toBe('POST');
    req.flush({ ok: true, data: null });

    expect(service.user()).toBeNull();
    expect(service.isAuthenticated()).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should fetch current user', () => {
    const mockUser = {
      id: '1',
      email: 'me@example.com',
      firstName: 'Me',
      lastName: 'User',
      active: true,
      staff: true,
      groups: ['admin'],
      permissions: ['item.view', 'category.view'],
    };

    service.fetchCurrentUser().subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/auth/me`);
    expect(req.request.method).toBe('GET');
    req.flush({ ok: true, data: mockUser });

    expect(service.isAuthenticated()).toBeTrue();
    expect(service.user()?.email).toBe('me@example.com');
  });

  it('should clear user when fetchCurrentUser fails', () => {
    service.fetchCurrentUser().subscribe();

    const req = httpMock.expectOne(`${environment.apiUrl}/api/auth/me`);
    req.error(new ProgressEvent('error'), { status: 401 });

    expect(service.isAuthenticated()).toBeFalse();
    expect(service.user()).toBeNull();
  });

  it('should check single permission correctly', () => {
    const mockUser = {
      id: '1',
      email: 'a@b.com',
      firstName: 'A',
      lastName: 'B',
      active: true,
      staff: false,
      groups: [],
      permissions: ['item.view', 'category.view'],
    };

    service.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({ ok: true, data: mockUser });

    expect(service.hasPermission('item.view')).toBeTrue();
    expect(service.hasPermission('item.delete')).toBeFalse();
  });

  it('should check hasAnyPermission correctly', () => {
    const mockUser = {
      id: '1',
      email: 'a@b.com',
      firstName: 'A',
      lastName: 'B',
      active: true,
      staff: false,
      groups: [],
      permissions: ['item.view'],
    };

    service.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({ ok: true, data: mockUser });

    expect(service.hasAnyPermission('item.view', 'category.view')).toBeTrue();
    expect(service.hasAnyPermission('category.view', 'form.view')).toBeFalse();
  });
});
