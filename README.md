# User Profile Service (Dev)

REST microservice developed with Spring Boot 4 and Java 25 for managing authenticated user profiles.

## Requirements

- **Java 25**
- **Gradle**

## How to Run

To start the application, execute the following command at the project root:

```bash
./gradlew bootRun
```

The application will be available by default at `http://localhost:8080`.

## API Documentation

### 1. Token Acquisition (Dev Mode)

To facilitate testing, the microservice includes a login endpoint that generates valid tokens without requiring complex password validation.

**Endpoint:** `POST /api/v1/auth/login`

**Example curl:**

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "john_doe"}'
```

**Response:**

```json
{
  "access_token": "eyJhbGci...",
  "token_type": "Bearer",
  "expires_in_seconds": 1800
}
```

---

### 2. User Profile Management

All endpoints under `/api/v1/profile` require the token obtained previously in the `Authorization` header.

#### Create Profile (POST)

Allows creating the profile associated with the token user. If it already exists, returns a 409 Conflict error.

**Example curl:**

```bash
curl -X POST http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <YOUR_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe@example.com",
           "firstName": "John",
           "lastName": "Doe",
           "birthDate": "1990-05-15",
           "phoneNumber": "555-0100",
           "street": "123 Main St",
           "city": "New York",
           "country": "USA",
           "postalCode": "10001"
         }'
```

#### Get My Profile (GET)

Returns the profile data of the authenticated user.

**Example curl:**

```bash
curl -X GET http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <YOUR_TOKEN>"
```

#### Update Profile (PUT - Full Replace)

Performs a complete replacement of the profile. Fields not included in the request will be set to null (if they are optional).

**Example curl:**

```bash
curl -X PUT http://localhost:8080/api/v1/profile \
     -H "Authorization: Bearer <YOUR_TOKEN>" \
     -H "Content-Type: application/json" \
     -d '{
           "email": "john.doe.updated@example.com",
           "firstName": "John Michael",
           "lastName": "Doe Smith",
           "birthDate": "1990-05-15"
         }'
```

---

## Design Notes and Decisions

- **User Identification**: The `userId` is never sent in the body or URL; it is extracted exclusively from the `sub` claim of the JWT.
- **H2 Database**: An in-memory database is used for development.
  - **H2 Console**: `http://localhost:8080/h2-console`
  - **JDBC URL**: `jdbc:h2:mem:testdb`
  - **Credentials**: `sa` / `password`
- **Validations**: Bean Validation is used to ensure data integrity (`@Email`, `@Past`, `@NotBlank`).
- **Error Handling**: A `@RestControllerAdvice` has been implemented to return consistent error responses (400, 401, 404, 409) with field details in case of validation errors.
- **PUT (Full Replace)**: Following HTTP PUT semantics, the update endpoint replaces the entire entity. It is the client's responsibility to send all fields it wishes to preserve.

## Assumptions and Tradeoffs

### Assumptions

- **Optional birthDate**: It is assumed that birth date is an optional field, as not all users may want to provide it for privacy reasons.
- **Globally unique email**: It is assumed that email must be unique throughout the system, not just per user.
- **Simplified authentication**: The login endpoint generates tokens without password validation, assuming a development/testing environment.
- **One profile per user**: It is assumed that each authenticated user can only have one profile associated with their `userId`.

### Tradeoffs

- **PUT full replace vs PATCH**: We opted for PUT with full replacement instead of partial PATCH for implementation simplicity and time constraints. The client must send all fields with each update.
- **H2 in-memory vs persistent DB**: Ease of setup and testing was prioritized over real data persistence. In production, PostgreSQL or similar would be recommended.
- **DTO layer validation**: Validations are performed on DTOs instead of the entity to have granular control over what is validated on creation vs update.
- **Hardcoded i18n messages in English**: Due to time constraints, only the English message file is included, although the infrastructure supports multiple languages.
- **Generic DataIntegrityViolationException handling**: It is assumed that any integrity violation is due to duplicate email. In a more robust system, the error message would be analyzed to give more specific feedback.

