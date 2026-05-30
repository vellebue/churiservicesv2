# churiservicesv2
This is a new microservices project based on IA technics

## Logging

All modules share a single Logback configuration provided by the `common`
module (`common/src/main/resources/logback-spring.xml`). Logs are emitted as
structured JSON with the following fields: `timestamp`, `level`, `logger`,
`thread`, `module`, `correlationId`, `transactionId`, `message`, and
`stack_trace` (only when an exception is logged).

### Log destination — Spring profiles

The destination of log output is chosen at runtime via the active Spring
profile:

| Active profile        | Destination                              | When to use                     |
|-----------------------|------------------------------------------|---------------------------------|
| _(none — default)_    | **stdout**                               | Containers (Docker Compose, k8s), CI |
| `local` / `dev-local` | **`./logs/${module-name}.log`** (rolling, gz, 10 MB × 5 archives) | Local development on the host  |

Rationale: inside a container the platform's logging driver captures stdout,
so writing files inside the container is an anti-pattern. On a developer
machine a rolling file on disk is convenient for grepping.

### How to activate the `local` profile

Any of the following works:

```bash
# Maven Spring Boot plugin
./mvnw spring-boot:run -pl :articles -Dspring-boot.run.profiles=local

# Packaged jar
java -jar articles/target/articles-0.0.1-SNAPSHOT.jar \
     -Dspring.profiles.active=local

# Environment variable
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run -pl :articles
```

Without any of these flags the service logs to stdout — which is what Docker
Compose and any container orchestrator expect.

### Correlation and transaction ids

Every HTTP request is tagged with:

- **`correlationId`** — extracted from the incoming W3C `traceparent` header
  (falls back to `X-Correlation-ID`, otherwise generated). Propagated on
  outbound HTTP calls automatically when using Spring's `RestClient` or
  `RestTemplate`.
- **`transactionId`** — a fresh UUID generated per microservice invocation,
  never propagated.

Both values are pushed into SLF4J MDC and therefore appear in every JSON log
entry produced during the request.

### Entry-point logging

Annotate controllers, event listeners, and queue receivers with
`@LogEntry` (from `common/infrastructure/logging`). The shared aspect logs:

- at **INFO** — entry-point name + elapsed time;
- at **DEBUG** — the (redacted) request payload.

Sensitive keys (`authorization`, `password`, `token`, `secret`, `apiKey`,
`creditCard`, `ssn`) are masked by the `LogRedactor`. The denylist can be
overridden per module via `logging.redaction.denylist` in `application.yml`.
