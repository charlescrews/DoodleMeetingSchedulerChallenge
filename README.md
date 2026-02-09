# Doodle-like Meeting Scheduler (Backend)

Backend service built with Spring Boot that simulates a simplified version of Doodle.  
It allows users to manage time slots, create meetings from available slots, and query free/busy availability in a given time range.

## Tech stack

- Java 17
- Spring Boot 3
  - Spring Web
  - Spring Data JPA
  - Spring Boot Actuator
- PostgreSQL
- Docker + Docker Compose
- OpenAPI / Swagger UI (API documentation)
- Maven

## Architecture and domain model

The design is intentionally simple (targeted at a ~4h challenge) but structured to be extendable:

- Layers:
  - `web`: REST controllers + request/response DTOs.
  - `service`: business logic (time slot management, meeting scheduling, availability).
  - `repository`: persistence layer using Spring Data JPA.
  - `domain`: core domain entities (`User`, `TimeSlot`, `Meeting`, `MeetingParticipant`).

- Core entities:
  - `User`:
    - `id`, `email`, `name`
  - `TimeSlot`:
    - `id`, `user`, `start`, `end`, `status` (`FREE` / `BUSY`), optional reference to `Meeting`
  - `Meeting`:
    - `id`, `title`, `description`, `organizer`, `slot`
  - `MeetingParticipant`:
    - `id`, `meeting`, `user`

Each user implicitly has a “personal calendar” represented by their collection of `TimeSlot` entries. The concept of “calendar” stays in the domain; there is no separate `calendar` table.

## Running with Docker Compose

Requirements:

- Docker
- Docker Compose
- (Optionally) Maven if you want to build locally outside Docker

