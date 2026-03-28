import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { AuthService } from '../../core/services/auth.service';
import { environment } from '../../../environments/environment';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let httpMock: HttpTestingController;
  let authService: AuthService;

  const mockUser = {
    id: '1',
    email: 'admin@example.com',
    firstName: 'Admin',
    lastName: 'User',
    active: true,
    staff: true,
    groups: ['admin'],
    permissions: ['product.view', 'category.view', 'form.view', 'workflow.view'],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);

    // Set up authenticated user
    authService.fetchCurrentUser().subscribe();
    httpMock.expectOne(`${environment.apiUrl}/api/auth/me`).flush({ ok: true, data: mockUser });

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display welcome message with user first name', () => {
    const welcome = fixture.nativeElement.querySelector('.welcome h2');
    expect(welcome.textContent).toContain('Admin');
  });

  it('should render dashboard cards for permitted features', () => {
    const cards = fixture.nativeElement.querySelectorAll('.dashboard-card');
    expect(cards.length).toBe(4);
    expect(cards[0].textContent).toContain('Products');
    expect(cards[1].textContent).toContain('Categories');
    expect(cards[2].textContent).toContain('Forms');
    expect(cards[3].textContent).toContain('Workflows');
  });
});
