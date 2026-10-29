# tairabek-akerke-student-api

Spring Boot CRUD REST API for managing students.
Author: **Akerke Tairabek**, group IT2-2307, student ID 38137.

## Stack
Java 17, Spring Boot 3, Gradle, PostgreSQL 16, Docker, Docker Compose, Jenkins.

## Endpoints
| Method | URL | Description |
|---|---|---|
| GET | `/` | App info (author, version) |
| GET | `/api/students` | All students |
| GET | `/api/students/{id}` | One student (404 if missing) |
| POST | `/api/students` | Create student |
| PUT | `/api/students/{id}` | Update student |
| DELETE | `/api/students/{id}` | Delete student |
| GET | `/api/students/search?lastName=Tairabek` | Search by last name |

## Run
```bash
cp .env.example .env        # set your own password
docker compose up -d --build
curl http://localhost:8080/
```

## Changes
- Akerke Tairabek: initial version of the Student API.
- Akerke Tairabek: added search by last name (GET /api/students/search).
