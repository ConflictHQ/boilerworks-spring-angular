# Claude -- Boilerworks Spring Boot + Angular

Primary conventions doc: [`bootstrap.md`](bootstrap.md)

Read it before writing any code.

## Stack

- **Backend**: Spring Boot 3.4 (Java 24, Gradle 8.14)
- **Frontend**: Angular 19 (TypeScript, standalone components, signals)
- **API**: REST with `ApiResponse<T>` wrapper
- **ORM**: Spring Data JPA (Hibernate 6)
- **Auth**: Spring Security session-based (httpOnly cookies, Redis sessions)
- **Database**: PostgreSQL 16 (Flyway migrations)
- **Cache/Sessions**: Redis 7

## Claude-specific notes

- Prefer `Edit` over rewriting whole files.
- Run `make test` before committing. Backend tests use H2 in-memory.
- Never expose integer PKs in API responses -- use UUID.
- Auth check (`@PreAuthorize`) is required on **every** controller method.
- Soft-delete only: set `deletedAt`/`deletedBy`, never call `repository.delete()` on business objects.
- `@SQLRestriction("deleted_at IS NULL")` on all soft-deletable entities.
- Lombok requires 1.18.38+ for Java 24 compatibility.
- Tests use `@Import(TestConfig.class)` to provide test beans without Redis.
- Use `new HashSet<>(Set.of(...))` for mutable sets in Hibernate entities, never `Set.of()`.

## REST API pattern

- Controllers: `@RestController @RequestMapping("/api/<resource>")`
- Mutations return `ApiResponse<T>` with `{ok, data, errors[{field, messages}]}`
- List endpoints return `List<Dto>` directly
- Permissions: `@PreAuthorize("hasAuthority('<resource>.<action>')")`

## Ports

| Service  | Port |
|----------|------|
| API      | 8000 |
| Angular  | 3000 |
| Postgres | 5432 |
| Redis    | 6379 |

## Quick start

```bash
make dev            # Start Postgres + Redis
make test-backend   # Run JUnit tests
make test-frontend  # Run Angular tests
make build          # Build both
```
