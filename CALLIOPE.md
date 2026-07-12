# Calliope — Boilerworks Spring Boot + Angular
<!-- Agent shim for https://github.com/calliopeai/calliope-cli -->

Primary conventions doc: [`bootstrap.md`](bootstrap.md)

Read it before writing any code.

---

## Project-specific notes

- Backend is Spring Boot 3.4 (Java 24, Gradle); frontend is Angular 19 (standalone components, signals).
- REST responses use the `ApiResponse<T>` wrapper `{ok, data, errors}`; auth is Spring Security session-based (Redis sessions, httpOnly cookies).
- `@PreAuthorize("hasAuthority('<resource>.<action>')")` is required on every controller method.
- UUID PKs only — never expose integer PKs. Soft-delete only (`deletedAt`/`deletedBy`), never `repository.delete()`; `@SQLRestriction("deleted_at IS NULL")` on soft-deletable entities.
- Use `new HashSet<>(Set.of(...))` for mutable Hibernate sets, never `Set.of()`; Lombok 1.18.38+ for Java 24.
- `make dev` + `./gradlew bootRun` + `npx ng serve`; backend tests use H2 in-memory.
