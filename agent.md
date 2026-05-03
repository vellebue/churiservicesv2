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

## Commits

- Use conventional commits to report commits.