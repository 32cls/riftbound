# AGENTS.md

## Repo

- Standalone git repo (remote: `32cls/riftbound`): a SpiritForge card-collection app forked from the Quarkus `getting-started-dev-services` quickstart.
- This directory is nested inside an unrelated `quarkus-quickstarts` checkout. Ignore the parent folder's README, CI, and config — they do not apply here.
- `README.md` is stale quickstart boilerplate. Trust `pom.xml` and `src/main/resources/application.properties` over prose.
- Stack: Quarkus 3.39.1, Java 17, single Maven module (no monorepo), Panache + PostgreSQL, SmallRye JWT (cookie) + smallrye-jwt-build, security-jpa (bcrypt users in `app_users`), Bean Validation.

## Commands

- Windows: use `mvnw.cmd` (README's `./mvnw` is for Unix).
- Dev mode (live coding, Dev UI at `http://localhost:8080/q/dev/`): `mvnw.cmd quarkus:dev`
- Tests: `mvnw.cmd test` (unit: `@QuarkusTest` classes)
- Integration tests are skipped by default (`skipITs=true` in pom); run with `mvnw.cmd verify -DskipITs=false` (or a `-Dnative` build).
- Native build (slow, needs GraalVM or `-Dquarkus.native.container-build=true`): `mvnw.cmd package -Dnative`
- No lint/formatter/typecheck tooling; compile (`mvnw.cmd compile` / `test`) is the check.

## How it's wired

- JWT travels in a **cookie** named `jwt` (`mp.jwt.token.header=Cookie`), issuer `https://example.com/issuer`. Tests/curl must send the cookie, not an `Authorization` header.
- `POST /auth/login` validates bcrypt against `app_users`, signs a token locally, returns it as the `jwt` cookie. `POST /auth/register` creates a user (role hardcoded `user`).
- The JWT `upn` claim holds the **user id**; `CardResource.addCard` resolves the owner via `ctx.getUserPrincipal().getName()` — don't break that contract.
- Entities (Panache): `Deck` → `Card` (owner `User`, ref `CardReference`). `CardResource.deleteDeck` requires role `user`; other endpoints are open by default (`smallrye.jwt.always-check-authorization=true`).

## Gotchas

- `import.sql` seeds two decks with Postgres-specific `nextval('Deck_SEQ')`. Quarkus auto-loads it **in dev mode only** — tests and prod start with an empty DB.
- Dev Services Postgres is pinned to port **5432** (`quarkus.datasource.devservices.port`); if 5432 is taken, dev startup fails.
- `privateKey.pem` (signing key for `/auth/login`) is **untracked and gitignored** (`*.pem`). Fresh clones cannot log in until the key is restored; only `publicKey.pem` is committed.
- `quarkus.native.resources.includes` lists only `publicKey.pem` — verify private-key signing still works in native mode before relying on it.
- Prod profile hardcodes credentials (`leopold`/`bloom`, `localhost:5432/mydatabase`). Override via env vars; never commit real secrets.
