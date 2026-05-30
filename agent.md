# Project Context: Spring Boot Kotlin Multi-module System

## 🎯 Overview
This is a microservices-based project built with **Spring Boot** and **Kotlin**, managed via **Maven** in a multi-module configuration. The system follows **Clean Architecture** principles to ensure decoupling and maintainability.

## 🏗️ Project Structure (Multi-module)
The project is organized into independent modules under the root directory:
- `[project-root]/pom.xml`: Parent POM managing versions and global dependencies.
- `[module-name]/src/main/kotlin/org/bastanchu/churiservicesv2/[module-name]/`: Each microservice follows this internal Clean Architecture structure:
    - `domain/`: Entities, value objects, repository interfaces (output ports), and domain exceptions. No dependencies on frameworks.
        - `exception/`: Domain-specific exceptions (e.g. `ArticleNotFoundException`, `InvalidArticleException`).
        - Repository interfaces live here directly (e.g. `ArticleRepository.kt`) — they are the output ports the application depends on; their implementations live in `infrastructure/persistence`.
    - `application/`: Use cases, DTOs, commands, and service implementations.
        - UseCase interfaces (input ports) live at the `application/` root, suffixed `UseCase` (e.g. `CreateArticleUseCase.kt`). Each defines a single `execute(...)` entry point.
        - `service/`: Service implementations of UseCases, suffixed `Service` (e.g. `CreateArticleService.kt`). Since PostgreSQL is the persistence engine, annotate services with `@Transactional` (use `@Transactional(readOnly = true)` for read-only operations).
        - `service/` also hosts mappers between domain entities and DTOs, implemented as Kotlin extension functions (`fun Article.toDto(): ArticleDto`) grouped per aggregate (e.g. `ArticleMapper.kt`).
        - `dto/`: DTOs returned from use cases. Use Kotlin `data class`es.
        - `command/`: Input commands for create/update use cases, suffixed `Command`. Immutable Kotlin `data class`es with `jakarta.validation` annotations on fields.
    - `infrastructure/`:
        - `persistence/`: JPA/Hibernate adapters — `*JpaEntity`, `*JpaRepository` (Spring Data interface), and `*RepositoryImpl` adapting the Spring Data repo to the domain repository interface.
        - `web/`: REST controllers (JSON) and the global exception handler. Use standard RESTful conventions; configure Hibernate Validator (`@Valid`) to validate inputs when required.
        - `config/`: Spring `@Configuration` beans local to the module (e.g. `MessageConfig`). Cross-cutting configs (security, etc.) live in the `common` module and are picked up by component scan.
        - `health/`: Implementations of domain health-check ports (e.g. `PostgresqlHealthChecker`).

## 🛠️ Technical Stack
- **Language:** Kotlin 1.9+
- **Framework:** Spring Boot 3.x
- **Build Tool:** Maven
- **Persistence:** Spring Data JPA / Hibernate
- **Communication:** RESTful APIs (JSON)
- **Serialization:** Jackson (Kotlin Module)

## 📋 Development Rules
1. **Dependency Direction:** Dependencies must point inwards. `Infrastructure` depends on `Application`, and `Application` depends on `Domain`. `Domain` must remain pure Kotlin.
2. **Kotlin Conventions:** 
    - Use `data classes` for DTOs and Domain Entities.
    - Use `val` by default; avoid `var` unless strictly necessary.
    - Use `PascalCase` for classes and `camelCase` for functions/variables.
3. **JPA/Hibernate:** 
    - Keep JPA annotations restricted to the `infrastructure` layer.
    - Map database entities to domain entities using mappers.
4. **Error Handling:** Use a global exception handler (`@RestControllerAdvice`) in `infrastructure/web` to map domain exceptions to standard HTTP status codes. Prefer Spring 6 `ProblemDetail` (RFC 7807) for response bodies.

## 🚀 Key Commands
- `./mvnw clean install`: Build all modules and run tests.
- `./mvnw spring-boot:run -pl :[module-name]`: Run a specific microservice.
- `./mvnw test`: Execute the full test suite.

## ⚠️ Constraints
- Do not add dependencies to the `domain` module.
- Always use constructor injection instead of `@Autowired`.
- Ensure all REST endpoints follow JSON naming conventions using `camelCase` for field names (Jackson default; matches Kotlin property naming and JS/TS clients).

## Documentation

- Use swagger to document JSON web services.
- Each web controller will be documented using swagger.

## Security

- **Model:** OAuth2 Resource Server with JWT bearer tokens. Identity provider is Keycloak (realm `churiservicesv2`); per-environment `issuer-uri` and `jwk-set-uri` go in each module's `application.yml` under `spring.security.oauth2.resourceserver.jwt`.
- **Where it lives:** A single `SecurityConfig` lives in `common/infrastructure/config/` and is shared across modules. Each module's `@SpringBootApplication` must declare `scanBasePackages = ["org.bastanchu.churiservicesv2"]` so the bean is picked up by component scan — do **not** duplicate `SecurityConfig` per module.
- **Session policy:** Stateless (`SessionCreationPolicy.STATELESS`); CSRF disabled (REST-only, no cookie sessions).
- **Default access rules:**
    - Public: `/v3/api-docs/**`, `/swagger-ui.html`, `/swagger-ui/**`, `/actuator/**`, `/api/ping`, `/api/ping/**`.
    - Everything else under `/api/**` requires a valid JWT.
    - When a new endpoint must be public, extend the allowlist in `SecurityConfig` — never disable security per-controller.
- **Authorities:** A `JwtAuthenticationConverter` merges OAuth2 scopes with Keycloak realm roles. Realm roles are mapped to authorities prefixed with `ROLE_` (e.g. realm role `articles_admin` → authority `ROLE_articles_admin`), so use `@PreAuthorize("hasRole('articles_admin')")` on protected methods.
- **Method security:** Prefer `@PreAuthorize` on UseCase service methods over URL-pattern rules when authorization depends on roles or arguments.

## Database and Infrastructure

- Use PostgreSQL v16 as database technology
- Define a global /docker folder and a /docker folder for each module. On each /docker folder put a docker compose file.
- For each module there will be a dedicated database defined on its docker compose file.
- For each module there will be a track record to maintain database scripts. Use flyway to manage those migrations.

## Tests

- Define unit tests for each Use Case.
- Define architecture tests to ensure clean architecture.

## Logs

- **Stack:** SLF4J as the logging facade, Logback as the backend, JSON encoding
    via `logstash-logback-encoder`. The shared `logback-spring.xml` and the
    encoder dependency live in the `common` module so every microservice
    inherits the same configuration via component scan / classpath.
- **Tracing context:**
    - Use the W3C Trace Context header `traceparent` to receive and propagate
      the **correlation id** (end-to-end across services). When `traceparent`
      is absent, generate one on entry.
    - Generate a per-call **transaction id** (UUID) at every microservice
      entry point. It is local to that service and is **not** propagated.
    - Distinction: `correlationId` ties a full distributed call chain;
      `transactionId` ties a single microservice invocation.
    - Both ids are populated into **SLF4J MDC** by a shared
      `OncePerRequestFilter` (HTTP) and a Kafka/queue `RecordInterceptor`
      (messaging), both defined in `common`. Never concatenate ids into log
      messages by hand — always rely on MDC.
    - Propagate `traceparent` on outbound HTTP calls (via a
      `RestClient`/`WebClient` interceptor) and on Kafka record headers.
- **Output destination:**
    - In containerized environments (the default — every module ships with
      Docker Compose), logs go to **stdout** so the Docker logging driver
      captures them. Do not write log files inside containers.
    - For local development, a rolling file appender writes to
      `./logs/${module-name}.log`.
- **Rotation policy (local file appender):**
    - `SizeAndTimeBasedRollingPolicy` — active file `${module-name}.log`,
      rotated files `${module-name}-%d{yyyy-MM-dd}.%i.log.gz`.
    - Max size 10 MB per file, maximum 5 archived files, oldest deleted.
- **Async appender:** wrap file/stdout appenders in `AsyncAppender` so log
    I/O never blocks request threads (especially during rotation).
- **Mandatory JSON fields per log entry:**
    - `timestamp` (ISO-8601, UTC)
    - `level` (ERROR / WARN / INFO / DEBUG)
    - `logger` (logger name)
    - `thread` (thread name)
    - `module` (microservice name)
    - `correlationId` (from MDC)
    - `transactionId` (from MDC)
    - `message`
    - `stack_trace` (only when an exception is logged)
    - `body` (optional, redacted — see PII rules below)
- **Log levels — when to use each:**
    - `ERROR`: unexpected failures requiring attention / alerting.
    - `WARN`: degraded but handled situations (retry succeeded, fallback used).
    - `INFO`: state transitions, entry-point invocations, lifecycle events.
    - `DEBUG`: developer diagnostics. Disabled in production.
- **Entry-point logging:**
    - Implemented via a `@LogEntry` AOP aspect (in `common`) applied to
      controllers, event listeners, and queue receivers. Controllers stay
      clean — no manual `log.info(...)` boilerplate per endpoint.
    - The aspect logs the entry-point name, HTTP method/route (or topic/queue
      name), and the request body **at DEBUG level only**.
    - At INFO level, only metadata is logged (entry-point name, ids, status),
      never full bodies.
- **PII and secret redaction:**
    - A redaction filter masks values for any field whose name matches the
      denylist: `authorization`, `password`, `token`, `secret`, `apiKey`,
      `creditCard`, `ssn`. Denylist lives in `common` and is overridable
      per module via configuration.
    - Headers `Authorization`, `Cookie`, `Set-Cookie` are never logged.
    - Never log full request/response bodies at INFO; DEBUG only, and only
      after redaction.

## Commits

- Use conventional commits to report commits.