# Housing

A Spring Boot backend project for a housing/booking system, with a focus on **distributed systems, concurrency, Redis, idempotency, and Saga-based event processing**.

## Tech Stack

- **Java 17**
- **Spring Boot 4.1.1**
- Spring Web
- Spring Data JPA
- Spring Data Redis
- MySQL
- Redis
- Lombok
- Validation
- Gradle

## Architecture

The application follows a layered Spring Boot architecture:

```
Controller
    ↓
Service
    ↓
Repository
    ↓
MySQL

Service / Business Flow
    ↓
Redis Read/Write Models
    ↓
Saga Event Publisher
    ↓
Redis
    ↓
Saga Event Consumer / Processor
```

The project uses Redis for read-side data, Saga messaging, idempotency-related lookups, and concurrency/distributed locking.

## Booking Flow

The booking flow validates the requested Airbnb and booking dates, checks availability under a Redis-based lock, calculates the total price, creates the booking with a `PENDING` status, persists it to MySQL, and writes a booking read model to Redis.

```text
Create Booking Request
        ↓
Validate Airbnb + Dates
        ↓
Acquire Redis Lock
        ↓
Check Availability
        ↓
Calculate Price
        ↓
Create Booking (PENDING)
        ↓
Save to MySQL
        ↓
Write Booking Read Model to Redis
```

## Idempotency

The project includes an idempotency service to support **safe handling of repeated booking requests**.

Idempotency is important in booking systems because the same request can be sent more than once due to client retries, network problems, or request timeouts. The goal is to prevent a retry from unintentionally creating another booking for the same operation.

### Idempotency Key

Each booking stores an `idempotencyKey`. The application has an `IIdempotencyService` abstraction with operations for:

- Checking whether an idempotency key has already been used
- Retrieving the booking associated with an idempotency key

The read-side lookup is backed by Redis through `RedisReadRepository`.

```text
Client Request
     |
     | idempotency key
     v
Idempotency Service
     |
     | Check existing booking
     v
Redis Booking Read Model
     |
     +---- Existing booking ----> Return existing booking
     |
     +---- No existing booking -> Continue booking flow
```

The current implementation contains the service abstraction and booking lookup by idempotency key. The `isIdempotencyKeyUsed` method is currently a placeholder and is intended to be completed as the idempotency workflow is extended.

## Saga Event Flow

The project contains a Saga event model, publisher, consumer, and processor for coordinating business operations across steps.

A Saga event contains information such as:

- `sagaId`
- `eventType`
- `step`
- `payload`
- `timestamp`
- `status`

The event lifecycle starts with a `PENDING` status and can be processed by the Saga workflow.

### Redis-based Saga Messaging

Saga events are serialized to JSON and pushed to the Redis `saga:event` queue.

```text
Business Service
      |
      | Create SagaEvent
      v
SagaEventPublisher
      |
      | Serialize to JSON
      v
Redis List: saga:event
      |
      | Consume
      v
SagaEventConsumer
      |
      v
SagaEventProcessor
```

The consumer periodically checks the Redis list and processes available Saga events.

## Redis

Redis is used as part of the application's distributed-system design.

Current use cases include:

- Saga event messaging
- Booking read models
- Idempotency-related booking lookups
- Distributed locking for availability checks
- Redis-backed application operations

### Redis-based Concurrency Control

The booking flow uses a Redis lock to coordinate concurrent availability checks.

The lock key is based on the Airbnb and requested date range:

```text
lock:availability:<airbnbId>:<checkInDate>:<checkOutDate>
```

The lock has a configured expiration time and is released after the availability operation completes.

This is intended to reduce conflicting concurrent booking attempts for the same Airbnb/date range.

## Read and Write Models

The application separates parts of its read and write access.

### Write Side

MySQL/JPA repositories are used for persistent application data, including:

- Airbnb
- Availability
- Booking
- User

### Read Side

Redis read models are used for fast retrieval of:

- Airbnb
- Availability
- Booking

Booking read models also contain the stored idempotency key, allowing the application to locate an existing booking using that key.

## Database

The application uses **MySQL** with **Spring Data JPA** for persistent application data.

The typical write flow is:

```text
REST API
   ↓
Service Layer
   ↓
JPA Repository
   ↓
MySQL
```

## Project Structure

The main source code is organized under:

```text
src/
└── main/
    ├── java/
    │   └── com/airbnb/housing/
    │       ├── config/
    │       ├── dtos/
    │       ├── models/
    │       │   └── readModels/
    │       ├── repositories/
    │       │   ├── read/
    │       │   └── write/
    │       ├── saga/
    │       ├── service/
    │       │   └── concurrency/
    │       └── utils/
    └── resources/
        └── application.properties
```

## Getting Started

### Prerequisites

Install:

- Java 17
- Gradle
- MySQL
- Redis

### Clone the repository

```bash
git clone https://github.com/sakshik0/Housing.git
cd Housing
```

### Configure the application

Update the database and Redis configuration in:

```text
src/main/resources/application.properties
```

Do not commit local passwords, API keys, or other secrets to the repository.

### Run the application

Using the Gradle wrapper:

```bash
./gradlew bootRun
```

On Windows:

```bash
gradlew.bat bootRun
```

## Build

To create the application build:

```bash
./gradlew clean build
```

To run tests:

```bash
./gradlew test
```

## Key Concepts Demonstrated

This project demonstrates practical backend and distributed-system concepts, including:

- Spring Boot REST APIs
- Layered architecture
- Spring Data JPA
- MySQL persistence
- Redis integration
- Redis read/write models
- Idempotency
- Idempotency key-based booking lookup
- Redis-based messaging
- Saga pattern
- Asynchronous event processing
- JSON serialization
- Distributed concurrency and locking
- Dependency injection
- Lombok constructor injection

## Future Improvements

Potential extensions to the project include:

- Complete the `isIdempotencyKeyUsed` implementation and integrate it into the booking request flow
- More complete Saga orchestration and compensation handling
- Kafka-based event streaming for higher-scale event-driven workflows
- Better observability with metrics and distributed tracing
- Docker-based local development
- Integration and end-to-end tests
- API documentation with OpenAPI/Swagger
- Stronger atomicity around idempotency-key creation/checking under concurrent requests

## Author

**Sakshi Kumari**

GitHub: https://github.com/sakshik0
