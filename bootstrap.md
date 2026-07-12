# Boilerworks Spring Boot + Angular -- Bootstrap

## Architecture

- **Backend**: Spring Boot 3.4 with Spring MVC REST, Spring Data JPA, Spring Security
- **Frontend**: Angular 19 with standalone components, signals, and lazy-loaded routes
- **Database**: PostgreSQL 16 via Flyway migrations
- **Sessions**: Redis 7 via Spring Session
- **Auth**: Session-based with httpOnly cookies, group-based permissions

## Conventions

### Backend

- **Base entity**: All business entities extend `AuditableEntity` (UUID id, createdAt/By, updatedAt/By, deletedAt/By)
- **Soft deletes**: Use `@SQLRestriction("deleted_at IS NULL")`, call `entity.softDelete(userId)`, never `repository.delete()`
- **No integer PKs**: UUID everywhere, never expose internal IDs
- **API wrapper**: All mutations return `ApiResponse<T>` with `{ok, data, errors[{field, messages}]}`
- **Permissions**: `@PreAuthorize("hasAuthority('<resource>.<action>')")` on every controller method
- **Slugs**: Auto-generated from name via `SlugUtil.slugify()`
- **Validation**: `@Valid` + Bean Validation at controller boundaries

### Frontend

- **Standalone components**: No NgModules
- **Signals**: Use Angular signals for reactive state
- **Lazy loading**: All feature routes are lazy-loaded
- **Auth guard**: `authGuard` checks authentication, `permissionGuard()` checks specific permissions
- **API calls**: All HTTP calls include `{ withCredentials: true }` for session cookies
- **Dark theme**: Boilerworks dark admin theme via global SCSS

### Testing

- Backend: JUnit 5 + MockMvc + H2 in-memory (tests do not require Docker)
- Frontend: Karma + ChromeHeadless
- Assert against database state, not hardcoded strings
- Test both allowed and denied permission cases
- No empty test bodies

### Ports

| Service  | Port |
|----------|------|
| API      | 8086 |
| Angular  | 4200 |
| Postgres | 5443 |
| Redis    | 6386 |

## Build Order

The template was built in this order — extend it the same way:

1. **Scaffolding** — Spring Boot project (Gradle), Spring Data JPA + Flyway, Angular app, Docker Compose (postgres, redis, api, ui), health check, Checkstyle.
2. **Auth + permissions** — Spring Security session auth (httpOnly cookies, Redis sessions), User/Group/Permission entities + seed data, `@PreAuthorize` on every controller method, Angular auth guard + permission service.
3. **Core API** — REST controllers with the `ApiResponse<T>` wrapper, JPA auditing (`AuditableEntity`), soft-delete pattern, Angular CRUD services + components.
4. **Forms engine** — form definitions, field types, validation, publish/submit lifecycle.
5. **Workflow engine** — workflow definition/instance entities, state machine transitions.
6. **Infrastructure + polish** — feature toggles, seed data, CI pipeline, docs.
