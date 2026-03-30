import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, HttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter, Router } from '@angular/router';
import { authInterceptor } from './auth.interceptor';

describe('authInterceptor', () => {
  let httpMock: HttpTestingController;
  let http: HttpClient;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting(),
        provideRouter([]),
      ],
    });
    httpMock = TestBed.inject(HttpTestingController);
    http = TestBed.inject(HttpClient);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should navigate to /login on 401 response', () => {
    spyOn(router, 'navigate');

    http.get('/api/test').subscribe({ error: () => {} });

    const req = httpMock.expectOne('/api/test');
    req.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should not redirect on non-401 errors', () => {
    spyOn(router, 'navigate');

    http.get('/api/test').subscribe({ error: () => {} });

    const req = httpMock.expectOne('/api/test');
    req.flush('Server Error', { status: 500, statusText: 'Internal Server Error' });

    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should pass through successful responses', () => {
    let responseData: any;
    http.get('/api/test').subscribe((data) => (responseData = data));

    const req = httpMock.expectOne('/api/test');
    req.flush({ ok: true });

    expect(responseData).toEqual({ ok: true });
  });
});
