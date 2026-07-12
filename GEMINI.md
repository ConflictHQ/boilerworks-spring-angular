# Gemini -- Boilerworks Spring Boot + Angular

Primary conventions doc: [`bootstrap.md`](bootstrap.md)

Read it before writing any code. The stack notes and rules in
[`CLAUDE.md`](CLAUDE.md) and [`AGENTS.md`](AGENTS.md) apply to all agents,
including Gemini:

- Auth check (`@PreAuthorize`) on **every** controller method
- UUID primary keys only -- never expose integer PKs
- Soft-delete only: `softDelete()`, never `repository.delete()` on business objects
- Mutations return `ApiResponse<T>`; list endpoints return `List<Dto>` directly
- Frontend: standalone components, signals, lazy-loaded routes, `{ withCredentials: true }`
- Run `make test` before committing
