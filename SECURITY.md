# Security Policy

## Reporting a Vulnerability

If you discover a security vulnerability in Boilerworks, please report it responsibly.

**Do not open a public issue.**

Instead, email **security@weareconflict.com** with:

- Description of the vulnerability
- Steps to reproduce
- Potential impact
- Suggested fix (if any)

We will acknowledge your report within 48 hours and aim to release a fix within 7 days for critical issues.

## Supported Versions

| Version | Supported |
| ------- | --------- |
| latest  | Yes       |

## Security Best Practices

When deploying Boilerworks:

- Change the default Postgres credentials (`boilerworks`/`boilerworks`) — set `DB_USER`/`DB_PASSWORD` and the matching values in `docker/docker-compose.yaml`
- Require a Redis password and do not expose the Redis port publicly (sessions are stored in Redis)
- Remove or re-password the seeded admin user (`admin@boilerworks.dev` / `admin123`, from `V2__seed_data.sql`) before going live
- Use HTTPS in production and mark the session cookie secure (`server.servlet.session.cookie.secure=true`, `http-only=true`, `same-site=lax`)
- Restrict CORS to your own origins in the Spring Security / MVC configuration
- Keep `@PreAuthorize` on every controller method — no unauthenticated endpoints beyond auth and health
