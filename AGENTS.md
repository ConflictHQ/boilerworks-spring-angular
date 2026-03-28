# Agent Instructions -- Boilerworks Spring Boot + Angular

## Before Writing Code

1. Read `CLAUDE.md` and `bootstrap.md`
2. Understand the permission model: group-based, never direct user permissions
3. Understand the soft-delete pattern: `@SQLRestriction`, `softDelete()`, never `delete()`
4. Understand the `ApiResponse<T>` wrapper pattern for mutations

## Key Rules

- Every controller method must have `@PreAuthorize`
- All entities extend `AuditableEntity` and use UUID primary keys
- Use `@Valid` on all request body parameters
- Mutations return `ApiResponse<T>`, lists return `List<Dto>` directly
- Frontend uses standalone components, signals, and lazy-loaded routes
- All HTTP calls include `{ withCredentials: true }`
- Lombok 1.18.38+ is required for Java 24 compatibility
- Tests use `@Import(TestConfig.class)` for test-specific beans
- Use `new HashSet<>()` for mutable sets in Hibernate entities

## Testing

```bash
make test           # Run all tests
make test-backend   # Backend only
make test-frontend  # Frontend only
```

## Running

```bash
make dev            # Start Postgres + Redis
cd backend && ./gradlew bootRun
cd frontend && npx ng serve
```
