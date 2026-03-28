# Boilerworks Spring Boot + Angular

Enterprise-grade Java backend with Angular frontend for banking, fintech, and regulated industries.

## Stack

- **Backend**: Spring Boot 3.4 / Java 24 / Gradle 8.14
- **Frontend**: Angular 19 / TypeScript / Standalone Components
- **Database**: PostgreSQL 16 (Flyway migrations)
- **Cache/Sessions**: Redis 7
- **Auth**: Session-based (httpOnly cookies, Spring Security)
- **Permissions**: Group-based (`@PreAuthorize`)
- **CI**: GitHub Actions

## Features

- Session-based authentication with group-based permissions
- Products + Categories CRUD with soft deletes and audit trails
- Forms engine (JSON schema, dynamic rendering, validation)
- Workflow engine (state machine, transitions, audit log)
- Boilerworks dark admin theme
- Docker Compose for full-stack development
- JUnit 5 + Karma test suites

## Quick Start

### Prerequisites

- Java 24+
- Node.js 22+
- Docker

### Development

```bash
# Start infrastructure
make dev

# Run backend (separate terminal)
cd backend && ./gradlew bootRun

# Run frontend (separate terminal)
cd frontend && npx ng serve

# Run all tests
make test
```

### Docker Compose

```bash
# Full stack
cd docker && docker compose up -d --build

# API: http://localhost:8086
# UI:  http://localhost:4200
```

### Default Credentials

- Email: `admin@boilerworks.dev`
- Password: `admin123`

## Project Structure

```
backend/
  src/main/java/com/boilerworks/api/
    config/          # Spring Security, JPA auditing, Redis session
    model/           # JPA entities (AuditableEntity, Product, Category, User, Group, Permission)
    repository/      # Spring Data JPA repositories
    service/         # Business logic
    controller/      # REST controllers
    dto/             # Request/response DTOs with ApiResponse<T> wrapper
    security/        # UserDetails, UserDetailsService, auth entry point
    forms/           # Forms engine (model, service, controller)
    workflow/        # Workflow engine (model, service, controller)
  src/main/resources/
    db/migration/    # Flyway SQL migrations
    application.yaml
  src/test/          # JUnit 5 tests

frontend/
  src/app/
    core/            # Services, guards, interceptors, models
    shared/          # Reusable components (sidebar, header, dynamic form)
    features/        # Feature modules (auth, dashboard, products, categories, forms, workflows)

docker/
  docker-compose.yaml
  Dockerfile.api
  Dockerfile.ui
  nginx.conf
```

## API Endpoints

| Method | Path | Description | Permission |
|--------|------|-------------|------------|
| POST | `/api/auth/login` | Login | Public |
| GET | `/api/auth/me` | Current user | Authenticated |
| POST | `/api/auth/logout` | Logout | Authenticated |
| GET/POST | `/api/products` | List/Create products | `product.view` / `product.add` |
| GET/PUT/DELETE | `/api/products/{id}` | Get/Update/Delete product | `product.view/change/delete` |
| GET/POST | `/api/categories` | List/Create categories | `category.view` / `category.add` |
| GET/PUT/DELETE | `/api/categories/{id}` | Get/Update/Delete category | `category.view/change/delete` |
| GET/POST | `/api/forms` | List/Create forms | `form.view` / `form.add` |
| POST | `/api/forms/{id}/publish` | Publish form | `form.change` |
| POST | `/api/forms/{id}/submit` | Submit form | `form.submit` |
| GET/POST | `/api/workflows` | List/Create workflows | `workflow.view` / `workflow.add` |
| POST | `/api/workflows/{id}/start` | Start workflow instance | `workflow.execute` |
| POST | `/api/workflows/instances/{id}/transition` | Transition workflow | `workflow.execute` |

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and the [stack primer](../primers/spring-angular/PRIMER.md).
