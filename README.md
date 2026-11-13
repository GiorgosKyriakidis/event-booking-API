# Event Booking API

A REST API for creating events and booking seats, built with **Java and Spring Boot**. It handles concurrent bookings safely, so the same seat is never sold twice.

## Tech stack

- Java 17+
- Spring Boot 3 (Spring Web, Spring Data JPA, Bean Validation)
- Hibernate
- PostgreSQL
- Maven (wrapper included)
- Lombok

## Features

- Full CRUD-style endpoints for events (create, list, get by id, delete)
- Seat booking per event with automatic seat count updates
- Request validation with clear per-field error messages
- Consistent JSON error responses with proper HTTP status codes (400, 404, 409)
- DTOs separate the API from the database entities, so clients can't set fields like `id` or `availableSeats`
- Transactional bookings: the seat update and the booking insert succeed or fail together
- Optimistic locking (`@Version`) to prevent overselling under concurrent requests

## Getting started

### Prerequisites

- JDK 17 or newer
- PostgreSQL running locally on port 5432

### 1. Create the database

```sql
CREATE DATABASE event_booking_database;
```

Tables are created automatically on startup (`spring.jpa.hibernate.ddl-auto=update`).

### 2. Set the database password

The password is read from the `DB_PASSWORD` environment variable and is never stored in the repo. The username and URL are in `src/main/resources/application.properties` (default: user `postgres`, `localhost:5432`).

Git Bash / Linux / macOS:

```bash
export DB_PASSWORD=your_password
```

Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_password"
```

In IntelliJ IDEA: **Run → Edit Configurations →** your app **→ Environment variables** → `DB_PASSWORD=your_password`.

### 3. Run the app

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`.

## API endpoints

| Method | Endpoint | Description | Success | Errors |
|--------|----------|-------------|---------|--------|
| GET | `/api/events` | List all events | 200 | |
| GET | `/api/events/{id}` | Get one event | 200 | 404 |
| POST | `/api/events` | Create an event | 201 | 400 |
| DELETE | `/api/events/{id}` | Delete an event | 204 | 404 |
| POST | `/api/events/{eventId}/bookings` | Book seats for an event | 201 | 400, 404, 409 |
| GET | `/api/events/{eventId}/bookings` | List bookings of an event | 200 | 404 |

### Example requests

Create an event:

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{"title":"Rock Concert","description":"Live music","location":"Athens","dateTime":"2027-09-20T21:00:00","totalSeats":100}'
```

Response:

```json
{
  "id": 1,
  "title": "Rock Concert",
  "description": "Live music",
  "location": "Athens",
  "dateTime": "2027-09-20T21:00:00",
  "totalSeats": 100,
  "availableSeats": 100
}
```

Book 2 seats:

```bash
curl -X POST http://localhost:8080/api/events/1/bookings \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Gio","customerEmail":"gio@example.com","seats":2}'
```

### Validation and error responses

Invalid input returns `400` with one message per field:

```json
{
  "title": "must not be blank",
  "totalSeats": "must be greater than or equal to 1"
}
```

Other errors return a single message:

```json
{ "error": "Event not found with id: 9999" }
```

| Status | Meaning |
|--------|---------|
| 400 | Invalid request body |
| 404 | Event not found |
| 409 | Not enough seats left, or a concurrent booking changed the event first |

## How concurrent bookings are handled

If two requests try to book the last seat at the same moment, both could read `availableSeats = 1` and both pass the check. To prevent this, `Event` has a `@Version` field (optimistic locking). Every update only succeeds if the row still has the version that was read. The slower request fails, its transaction is rolled back, and the client receives `409 Conflict`.

Test it yourself by creating an event with 1 seat and sending 10 booking requests in parallel:

```bash
ID=1
for i in 1 2 3 4 5 6 7 8 9 10; do
  curl -s -o /dev/null -w "%{http_code}\n" -X POST http://localhost:8080/api/events/$ID/bookings \
    -H "Content-Type: application/json" \
    -d '{"customerName":"User'$i'","customerEmail":"u'$i'@test.com","seats":1}' &
done
wait
```

Expected result: exactly one `201` and nine `409`.

## Project structure

```
src/main/java/com/example/eventbookingapi
├── controller   REST endpoints (EventController, BookingController)
├── service      Business logic (EventService, BookingService)
├── repository   Spring Data JPA repositories
├── model        JPA entities (Event, Booking)
├── dto          Request and response objects
└── exception    Custom exceptions and the global exception handler
```

## Tests

```bash
./mvnw test
```

The current test checks that the application context starts, so it needs a running PostgreSQL and the `DB_PASSWORD` variable.

## Known limitations and roadmap

- Deleting an event that has bookings currently fails with a server error. It should return a clear `409`.
- Pagination for the list endpoints
- API documentation with Swagger / springdoc-openapi
- Authentication with Spring Security and JWT
- Integration tests with Testcontainers
- Database migrations with Flyway
