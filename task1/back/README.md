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

#### Register a new user
- **URL**: `POST /api/auth/signup`
- **Auth**: None
- **Request Body**:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```
- **Success Response (200 OK)**:
```json
{
  "message": "User registered successfully!"
}
```
- **Error Response (400 Bad Request)**:
```json
{
  "message": "Error: Email is already taken!"
}
```

#### Login and get JWT token
- **URL**: `POST /api/auth/signin`
- **Auth**: None
- **Request Body**:
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```
- **Success Response (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com"
}
```
- **Error Response (401 Unauthorized)**:
```json
{
  "message": "Invalid credentials"
}
```

### Users

#### Get all users
- **URL**: `GET /api/users`
- **Auth**: Bearer Token
- **Success Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Leanne Graham",
    "username": "bret",
    "email": "leanne@example.com",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }
]
```

#### Get user by ID
- **URL**: `GET /api/users/{id}`
- **Auth**: Bearer Token
- **Success Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Leanne Graham",
  "username": "bret",
  "email": "leanne@example.com",
  "address": {
    "street": "Kulas Light",
    "suite": "Apt. 556",
    "city": "Gwenborough",
    "zipcode": "92998-3874",
    "geo": {
      "lat": "-37.3159",
      "lng": "81.1496"
    }
  },
  "phone": "1-770-736-8031 x56442",
  "website": "hildegard.org",
  "company": {
    "name": "Romaguera-Crona",
    "catchPhrase": "Multi-layered client-server neural-net",
    "bs": "harness real-time e-markets"
  }
}
```
- **Error Response (404 Not Found)**: Empty response

#### Create a new user
- **URL**: `POST /api/users`
- **Auth**: Bearer Token
- **Request Body**:
```json
{
  "name": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "phone": "123-456-7890",
  "website": "johndoe.com",
  "address": {
    "street": "123 Main St",
    "suite": "Apt 456",
    "city": "Anytown",
    "zipcode": "12345",
    "geo": {
      "lat": "40.7128",
      "lng": "-74.0060"
    }
  },
  "company": {
    "name": "ABC Corp",
    "catchPhrase": "Innovation for everyone",
    "bs": "target end-to-end solutions"
  }
}
```
- **Success Response (200 OK)**:
```json
{
  "id": 3,
  "name": "John Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "address": {
    "street": "123 Main St",
    "suite": "Apt 456",
    "city": "Anytown",
    "zipcode": "12345",
    "geo": {
      "lat": "40.7128",
      "lng": "-74.0060"
    }
  },
  "phone": "123-456-7890",
  "website": "johndoe.com",
  "company": {
    "name": "ABC Corp",
    "catchPhrase": "Innovation for everyone",
    "bs": "target end-to-end solutions"
  }
}
```

#### Update an existing user
- **URL**: `PUT /api/users/{id}`
- **Auth**: Bearer Token
- **Request Body**:
```json
{
  "name": "John Doe Updated",
  "username": "johndoe",
  "email": "john@example.com",
  "phone": "123-456-7890",
  "website": "johndoe-updated.com",
  "address": {
    "street": "456 New St",
    "suite": "Suite 789",
    "city": "Newtown",
    "zipcode": "54321",
    "geo": {
      "lat": "41.8781",
      "lng": "-87.6298"
    }
  },
  "company": {
    "name": "XYZ Corp",
    "catchPhrase": "New innovation for everyone",
    "bs": "synergize end-to-end solutions"
  }
}
```
- **Success Response (200 OK)**:
```json
{
  "id": 3,
  "name": "John Doe Updated",
  "username": "johndoe",
  "email": "john@example.com",
  "address": {
    "street": "456 New St",
    "suite": "Suite 789",
    "city": "Newtown",
    "zipcode": "54321",
    "geo": {
      "lat": "41.8781",
      "lng": "-87.6298"
    }
  },
  "phone": "123-456-7890",
  "website": "johndoe-updated.com",
  "company": {
    "name": "XYZ Corp",
    "catchPhrase": "New innovation for everyone",
    "bs": "synergize end-to-end solutions"
  }
}
```
- **Error Response (404 Not Found)**: Empty response

#### Delete a user
- **URL**: `DELETE /api/users/{id}`
- **Auth**: Bearer Token
- **Success Response (200 OK)**:
```json
{
  "message": "User deleted successfully"
}
```
- **Error Response (404 Not Found)**: Empty response

## Testing

Run the tests using:

```bash
./gradlew test
``` 