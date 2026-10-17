# Housing — Event-Driven Booking Backend

A Spring Boot backend project exploring saga-style event processing for a housing/booking domain. The current implementation includes a small Redis-backed queue for publishing and consuming saga events, alongside the project's JPA/MySQL foundation.

> **Project status:** This is a work in progress. The saga publisher currently enqueues events and the scheduled consumer reads and logs them. Full business-step orchestration, durable processing/retry behavior, and idempotency enforcement are not implemented yet.

## Tech Stack

- **Java 17**
- **Spring Boot 4.1.1**
- **Gradle**
- **Spring Web MVC**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Spring Data Redis**
- **Jackson** for event JSON serialization
- **Lombok**

## Current Saga Event Flow

The current implementation uses a Redis list named `saga:event` as a simple queue:

1. `SagaEventPublisher` creates a `SagaEvent` with a generated saga ID, event type, step, payload, timestamp, and `PENDING` status.
2. The event is serialized to JSON.
3. The publisher appends the JSON to the Redis list using `rightPush`.
4. `SagaEventConsumer` runs on a scheduled interval and retrieves an item using `leftPop`.
5. The consumer deserializes the event and currently logs it.

```text
Application code
     |
     v
SagaEventPublisher
     |
     | JSON + RPUSH
     v
Redis list: saga:event
     |
     | scheduled LPOP
     v
SagaEventConsumer
     |
     v
Deserialize and log event
```

This demonstrates basic event handoff, but it is **not yet a complete saga orchestrator**. The consumer does not currently execute business steps, publish follow-up events, update saga state in persistent storage, or perform compensating actions.

## SagaEvent Model

The `SagaEvent` model currently contains:

| Field | Purpose |
| --- | --- |
| `sagaId` | Identifier assigned to the saga event |
| `eventType` | Describes the kind of event |
| `step` | Identifies the saga step associated with the event |
| `payload` | Event data represented as a key/value map |
| `timestamp` | Time the event was created |
| `status` | Event status: `PENDING`, `COMPLETED`, or `FAILED` |

## Idempotency

Idempotency is an important reliability requirement for event-driven systems: processing the same event more than once should not repeat its business effect. This matters when a producer retries, a message is delivered again, or a consumer restarts.

**Current status:** The repository's current saga publisher/consumer code does not yet implement idempotency or duplicate-event detection. The generated `sagaId` is an identifier, but by itself it does not prevent duplicate processing.

A robust next step would be to:
- Give each business operation/event a stable unique ID that remains the same across retries.
- Record processed event IDs in durable storage with a unique constraint, or use an equivalent atomic deduplication mechanism.
- Check and claim the event atomically before applying its business effect.
- Make the business update and processed-event record transactional where possible.
- Treat repeated events as already processed, and define retry handling for failures.
- Avoid relying on a Redis check-then-act sequence alone, because concurrent consumers can race.

For production-grade processing, also consider Redis Streams consumer groups or a durable message broker, acknowledgement/recovery behavior, retries, a dead-letter strategy, and persistent saga state. These are improvement areas, not claims about current functionality.

## Prerequisites

Install the following locally:

- JDK 17
- A running MySQL instance
- A running Redis instance on `localhost:6379` (or update the application configuration)

## Configure and Run

1. Clone the repository:

   ```bash
   git clone https://github.com/sakshik0/Housing.git
   cd Housing
   ```

2. Configure the database and Redis settings in `src/main/resources/application.properties`.

   The application is configured for a MySQL database named `Airbnb`, Redis at `localhost:6379`, and application port `3030`. Make sure the database exists and your local database credentials are configured before starting the app.

   **Security note:** Do not commit real database passwords or other secrets. Keep credentials in local environment-specific configuration or environment variables, and rotate any credential that has already been committed to a repository.

3. Start the application using the Gradle wrapper, if present:

   ```bash
   ./gradlew bootRun
   ```

   Or run `HousingApplication` from your IDE. If the Gradle wrapper is not included, install Gradle and run:

   ```bash
   gradle bootRun
   ```

## Configuration Reference

| Setting | Current value / purpose |
| --- | --- |
| `spring.application.name` | `housing` |
| `spring.datasource.url` | Local MySQL URL for database `Airbnb` |
| `spring.data.redis.host` | `localhost` |
| `spring.data.redis.port` | `6379` |
| `server.port` | `3030` |
| `spring.jpa.hibernate.ddl-auto` | `update` |

For local development, `spring.jpa.hibernate.ddl-auto=update` can update the schema automatically. Review schema migration tooling and safer schema-management settings before using this project in a production environment.

## Project Structure

Relevant saga classes currently include:

```text
src/main/java/com/airbnb/housing/
├── HousingApplication.java
└── saga/
    ├── SagaEvent.java
    ├── SagaEventPublisher.java
    └── SagaEventConsumer.java
```

## Planned Improvements

- Implement actual saga steps and state transitions.
- Add durable saga state and recovery after application restarts.
- Implement idempotent event handling and duplicate detection.
- Add retry policies, failure handling, and compensation logic.
- Add automated tests for publishing, consuming, duplicate events, and failures.
- Add structured logging and observability.

## License

No license information has been specified yet.