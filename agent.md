# Project Context: Spring Boot Kotlin Multi-module System

## 🎯 Overview
This is a microservices-based project built with **Spring Boot** and **Kotlin**, managed via **Maven** in a multi-module configuration. The system follows **Clean Architecture** principles to ensure decoupling and maintainability.

## 🏗️ Project Structure (Multi-module)
The project is organized into independent modules under the root directory:
- `[project-root]/pom.xml`: Parent POM managing versions and global dependencies.
- `[module-name]/org.bastanchu.churiservicesv2.$module-name`: Each microservice follows this internal Clean Architecture structure:
    - `domain`: Entities, value objects, and repository interfaces (No dependencies on frameworks).
    - `application`: Use cases, input/output ports, and DTOs. Use Cases must be divided into UseCase interface (suffixed as UseCase) and service implementation (suffixed as Service).
      - service: Put service implementations here. Due Postgresql is used please anotate services as @Transactional
    - `infrastructure`: 
      - `persistence`  Persistence (JPA/Hibernate) 
      - `web` REST controllers (JSON), and external adapters. User standard RESTful conventions for web services.

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
4. **Error Handling:** Use a global exception handler in the infrastructure layer to map domain exceptions to standard HTTP status codes.

## 🚀 Key Commands
- `./mvnw clean install`: Build all modules and run tests.
- `./mvnw spring-boot:run -pl :[module-name]`: Run a specific microservice.
- `./mvnw test`: Execute the full test suite.

## ⚠️ Constraints
- Do not add dependencies to the `domain` module.
- Always use constructor injection instead of `@Autowired`.
- Ensure all REST endpoints follow JSON naming conventions (typically `snake_case` or `camelCase` depending on your preference—please specify).

## Documentation

    - Use swagger to document JSON web services.
    - Each web controller will be documented using swagger.

## Database and Infrastructure

  - Use Posgresql v16 as database tecnology
  - Define a global /docker folder and a /docker folder for each module. On each /docker folder put a docker compose file.
  - For each module there will be a dedicated database defined on its docker compose file.
  - For each module there will be a track record to maintain database scripts. Use flyway to manage those migrations.

## Tests

- Define unit tests for each Use Case.
- Define architecture tests to ensure clean architecture.

## Commits

- Use conventional commits to report commits.