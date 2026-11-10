# Courier Tracking System

A courier service platform: a sender creates a shipment, the system assigns it to a courier in the recipient's zone, and the recipient follows the shipment history and the courier's live location on a map using a tracking number.

> 🚧 Work in progress. Current phase: system design (diagrams in `docs/diagrams`).

## Tech stack

Java 21 · Spring Boot 3 · PostgreSQL · Apache Kafka · Redis · Angular · Docker

## Documentation

- [Architecture](docs/architecture.md)
- [Use cases](docs/use-cases.md)
- [Class diagram](docs/class-diagram.md)
- [Database](docs/database.md)

## Running locally

1. Start the database: `docker compose up -d`
2. Run `shipment-service` from IntelliJ (`ShipmentServiceApplication`)
