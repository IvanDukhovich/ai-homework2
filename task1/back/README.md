# Back - User API with JWT Authentication

This is a RESTful API for user entity operations with JWT-based authentication, structured user data storage, and containerized deployment.

## Features

- User CRUD operations
- JWT authentication
- Structured data storage with PostgreSQL
- Docker containerization
- Unit and integration tests

## Tech Stack

- Java 17
- Spring Boot 3.1.5
- Spring Security with JWT
- MyBatis ORM
- PostgreSQL
- Docker & Docker Compose
- JUnit 5

## Project Structure

```
back/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── back/
│   │   │               ├── BackApplication.java
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               ├── exception/
│   │   │               ├── mapper/
│   │   │               ├── model/
│   │   │               ├── security/
│   │   │               └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/
│   │           └── migration/
│   └── test/
│       └── java/
├── build.gradle
├── settings.gradle
├── Dockerfile
└── docker-compose.yml
```

## Getting Started

### Prerequisites

- Java 17
- Docker and Docker Compose

### Running the Application

#### Using Docker Compose

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Docker Compose:

```bash
docker-compose up
```

#### Using Gradle

1. Clone the repository
2. Navigate to the project directory
3. Start a PostgreSQL instance:

```bash
docker-compose up -d db
```

4. Run the application:

```bash
./gradlew bootRun
```

## API Endpoints

### Authentication

- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/signin` - Login and get JWT token

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update an existing user
- `DELETE /api/users/{id}` - Delete a user

## Testing

Run the tests using:

```bash
./gradlew test
``` 