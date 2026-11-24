# Courier Tracking System

![CI](https://github.com/lazarlukic00/courier-tracking-system/actions/workflows/ci.yml/badge.svg)

A courier service platform: a sender creates a shipment, the system assigns it to a courier in the recipient's zone, and the recipient follows the shipment history and the courier's live location on a map using a tracking number.

> 🚧 **Work in progress.** Phase 1 (Shipment Service foundation) is complete. Next: shipments and public tracking.

## Tech stack

Java 21 · Spring Boot 3 · Spring Security (JWT) · PostgreSQL · Flyway · Docker · JUnit 5 · Mockito · GitHub Actions · OpenAPI/Swagger

Planned: Apache Kafka · Redis · Angular · Testcontainers

## Progress

| Phase | Scope | Status |
| --- | --- | --- |
| 0 | System design: use case, class, ER, component, sequence and state diagrams | ✅ Done |
| 1 | Shipment Service foundation: PostgreSQL in Docker, Flyway, JWT authentication, error handling, Swagger, unit tests, CI | ✅ Done |
| 2 | Shipments, tracking numbers, status transitions, public tracking, Angular frontend | ⏳ Next |
| 3 | Kafka, Dispatch Service, courier assignment, Redis, live map | ⬜ Planned |
| 4 | Reliability: Transactional Outbox, idempotent consumers, Testcontainers | ⬜ Planned |
| 5 | Admin features, deployment, demo data | ⬜ Planned |

## Features implemented

- User registration and login with BCrypt password hashing
- Stateless JWT authentication with a custom Spring Security filter
- Consistent JSON error responses (400, 401, 403, 404, 409) with field-level validation errors
- Separate PostgreSQL database and user per service, schema versioned with Flyway
- Interactive API documentation with JWT support (Swagger UI)
- Unit tests for authentication and token handling, run on every push by GitHub Actions

## API

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | – | Register a new sender account |
| POST | `/api/auth/login` | – | Log in and receive a JWT access token |
| GET | `/api/users/me` | Bearer token | Get the currently logged-in user |

Full interactive documentation: http://localhost:8081/swagger-ui.html

## Running locally

Requirements: Docker Desktop and Java 21.

1. Start the database:
   ```bash
   docker compose up -d
   ```
2. Run the Shipment Service from your IDE (`ShipmentServiceApplication`) or with Maven:
   ```bash
   cd shipment-service
   mvn spring-boot:run
   ```
3. Open the API documentation: http://localhost:8081/swagger-ui.html

Run the tests:
```bash
cd shipment-service
mvn test
```

## Documentation

- [Architecture](docs/architecture.md): component, sequence and state diagrams
- [Use cases](docs/use-cases.md)
- [Class diagram](docs/class-diagram.md)
- [Database](docs/database.md): ER diagram and design decisions
