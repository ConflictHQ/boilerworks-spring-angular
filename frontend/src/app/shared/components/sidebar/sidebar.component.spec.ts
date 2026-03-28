import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { SidebarComponent } from './sidebar.component';
import { AuthService } from '../../../core/services/auth.service';
import { environment } from '../../../../environments/environment';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  const mockUser = {
    id: '1',
    email: 'test@example.com',
    firstName: 'Test',
    lastName: 'User',
    active: true,
    staff: false,
    groups: [],
    permissions: ['product.view', 'category.view'],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

    // Set up authenticated user
    authService.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({ ok: true, data: mockUser });

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display brand name', () => {
    const brand = fixture.nativeElement.querySelector('.sidebar-brand h2');
    expect(brand.textContent).toContain('Boilerworks');
  });

  it('should show nav items based on permissions', () => {
    const navItems = fixture.nativeElement.querySelectorAll('.nav-item');
    // Dashboard (no permission needed) + Products + Categories = 3
    expect(navItems.length).toBe(3);

    const labels = Array.from(navItems).map((el: any) =>
      el.querySelector('.nav-label').textContent.trim()
    );
    expect(labels).toContain('Dashboard');
    expect(labels).toContain('Products');
    expect(labels).toContain('Categories');
    expect(labels).not.toContain('Forms');
    expect(labels).not.toContain('Workflows');
  });

  it('should display user info', () => {
    const userName = fixture.nativeElement.querySelector('.user-name');
    const userEmail = fixture.nativeElement.querySelector('.user-email');
    expect(userName.textContent).toContain('Test');
    expect(userName.textContent).toContain('User');
    expect(userEmail.textContent).toContain('test@example.com');
  });

  it('should call logout on button click', () => {
    spyOn(authService, 'logout').and.callThrough();
    const logoutBtn = fixture.nativeElement.querySelector('.logout-btn');
    logoutBtn.click();

    // The logout call triggers an HTTP request
    httpMock.expectOne(`${environment.apiUrl}/api/auth/logout`).flush({ ok: true, data: null });

    expect(authService.logout).toHaveBeenCalled();
  });
});
