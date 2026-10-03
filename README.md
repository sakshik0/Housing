# Housing

A Spring Boot backend project for a housing/booking system, with a focus on **distributed systems, concurrency, Redis, and Saga-based event processing**.

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
Saga Event Publisher
    ↓
Redis
    ↓
Saga Event Consumer / Processor
```

The project also uses Redis for supporting distributed-system concerns such as messaging and concurrency.

## Saga Event Flow

The project contains a Saga event model and publisher for coordinating business operations across steps.

A saga event contains information such as:

- `sagaId`
- `eventType`
- `step`
- `payload`
- `timestamp`
- `status`

The event lifecycle starts with a `PENDING` status and can be processed by the saga workflow.

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
Saga Event Processing
```

This provides asynchronous communication between the component producing the saga event and the component processing it.

## Redis

Redis is used as part of the application's distributed-system design.

Current use cases include:

- Saga event messaging
- Redis-backed operations used by the application
- Supporting concurrency/distributed locking patterns where required

Redis is useful here because it provides fast in-memory operations and can support lightweight asynchronous communication and distributed coordination.

## Database

The application uses **MySQL** with **Spring Data JPA** for persistent application data.

The typical data flow is:

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
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       ├── entity/
    │       └── saga/
    └── resources/
        └── application.properties
```

The exact package structure may evolve as new features are added.

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

This project is intended to demonstrate practical backend and distributed-system concepts, including:

- Spring Boot REST APIs
- Layered architecture
- Spring Data JPA
- MySQL persistence
- Redis integration
- Redis-based messaging
- Saga pattern
- Asynchronous event processing
- JSON serialization
- Distributed concurrency concepts
- Dependency injection
- Lombok constructor injection

## Future Improvements

Potential extensions to the project include:

- Authentication and authorization
- More complete Saga orchestration and compensation handling
- Kafka-based event streaming for higher-scale event-driven workflows
- Better observability with metrics and distributed tracing
- Docker-based local development
- Integration and end-to-end tests
- API documentation with OpenAPI/Swagger

## Author

**Sakshi Kumari**

GitHub: https://github.com/sakshik0
