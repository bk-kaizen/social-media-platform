# Social Media Platform

This is a Spring Boot-based social media platform application with user authentication and management features. The
project is structured to follow best practices for security and maintainability.

## Table of Contents

1. [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Building the Project](#building-the-project)
    - [Docker Compose](#docker-compose)
    - [Running the Application](#running-the-application)
2. [Configuration](#configuration)
3. [Security](#security)
    - [JWT Authentication](#jwt-authentication)
4. [Post Management APIs](#post-management-apis)
    - [Endpoints](#endpoints)
5. [Postman Documentation](#postman-documentation)
6. [Caching](#caching)

## Getting Started

### Prerequisites

- Java 17 or higher
- Docker (for containerization)
- Maven

### Building the Project

1. Clone the repository:

   ```sh
   git clone git@github.com:bk-kaizen/social-media-platform.git
   cd social-media-platform
   ```

2. Build the project with Maven:

   ```sh
   ./mvnw clean install
   ```

### Docker Compose

The docker-compose.yml file is used to set up the necessary services like Postgresql and redis for running the
application.

To start the services:

```sh
docker-compose up -d
```

Option `-d` added for detached mode.

### Running the Application

Using Maven:

```sh
./mvnw spring-boot:run
```

### Verify Application Status

After the application has started, you can verify its status using the following endpoint:
Health Status: http://localhost:8080/actuator/health

### Documentation

Access the API documentation using this endpoint:
Swagger API Documentation: http://localhost:8080/api/swagger

## Configuration

The application uses a properties file located at `src/main/resources/application.properties`. Adjust the configuration
as needed for your environment.

## Security

The application uses JWT for authentication and authorization. The security configuration is managed
in `SecurityConfiguration.java`.

## User Management APIs

JWT (JSON Web Token) is used for securing the APIs. The authentication process involves the following steps:

1. **User Registration**: Register a new user to obtain credentials.
2. **User Login**: Authenticate with the credentials to receive a JWT token.
3. **Accessing APIs**: Use the JWT token to access protected APIs.

#### EndPoints

**Register a User:**

`POST /api/auth/register`

Request Body:

```json
{
  "firstname": "john",
  "lastname": "doe",
  "email": "john@gmail.com",
  "password": "12345678"
}
```

**Login to get JWT Token:**

`POST /api/auth/login`

Request Body:

```json
{
  "username": "user",
  "password": "password"
}
```

The response will contain a JWT token:

```json
{
  "token": "your-jwt-token"
}
```

**Retrieve User Profile:**

`GET /api/users/:userid`

The response will contain a User profile details:

``` json
{
    "email": "john@gmail.com",
    "firstName": "john",
    "lastName": "doe"
}
```

**Access Protected APIs:**

Use the JWT token in the Authorization header as a Bearer token:

```sh
Authorization: Bearer your-jwt-token
```

## Post Management APIs

The Post Management APIs allow you to create, retrieve, update, and delete posts. The APIs are protected by JWT
authentication.

### Endpoints

**Create a Post:**

```sh
POST /api/posts
Authorization: Bearer your-jwt-token
Content-Type: application/json
```

Request Body:

```json
{
  "content": "This is a new post",
  "userId": "user-uuid"
}
```

Response Body

```json
{
  "id": 6,
  "content": "This is a post",
  "userId": "5e40bc30-82e4-43dd-b50c-5eef0e9fe9d4"
}
```

**Retrieve Posts:**

```sh
GET /api/posts
Authorization: Bearer your-jwt-token
```

Response Body

```json
{
  "totalElements": 5,
  "totalPages": 1,
  "size": 8,
  "content": [
    {
      "id": 6,
      "content": "This is a post",
      "userId": "5e40bc30-82e4-43dd-b50c-5eef0e9fe9d4"
    },
    {
      "id": 5,
      "content": "This is a post",
      "userId": "5e40bc30-82e4-43dd-a50c-5eef0e9fe9d4"
    }
  ],
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "pageable": {
    "pageNumber": 0,
    "pageSize": 8,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "first": true,
  "last": true,
  "numberOfElements": 5,
  "empty": false
}
```

**Retrieve a Post by ID:**

```sh
GET /api/posts/{postId}
Authorization: Bearer your-jwt-token
```

Response Body

```json
{
  "id": 6,
  "content": "This is a post",
  "userId": "5e40bc30-82e4-43dd-b50c-5eef0e9fe9d4"
}

```

**Update a Post:**

```sh
PUT /api/posts/{postId}
Authorization: Bearer your-jwt-token
Content-Type: application/json
```

Request Body:

```json
{
  "content": "Updated post content",
  "userId": "user-uuid"
}
```

Response Body

```json
{
  "id": 6,
  "content": "This is a updated post",
  "userId": "5e40bc30-82e4-43dd-b50c-5eef0e9fe9d4"
}

```

**Delete a Post:**

```sh
DELETE /api/posts/{postId}
Authorization: Bearer your-jwt-token
```

Response Says `200` OK.

## Postman Documentation

For detailed API documentation and example requests, refer to the Postman collection located in the `docs` directory.

## Caching

The application integrates Redis caching to improve response times for frequently accessed endpoints.
Caching is applied to the retrieval of post.

# Notes

- Implemented custom JWT flow authentication using `jjwt-api` dependencies.
- Simplified post management by excluding image or media content.
- Added API documentation in the Controller using `springdoc-openapi-starter-webmvc-ui`.
