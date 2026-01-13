# Task: Authenticated User Profile Service

## Goal

Deliver a REST microservice that manages **a single resource: the authenticated user’s profile**. The user identity **must be derived exclusively from a Bearer JWT** (e.g., the `sub` claim). Clients must not be able to choose or override the user identity.

## Scope

* Resource: **UserProfile** (one profile per authenticated user)
* Base path: **`/api/v1`**
* Auth: **Spring Security + Bearer JWT** (signature + expiration validation)
* Persistence: **H2 + Spring Data JPA** (preferred)

## Core Rules

* **No `userId` in URL** (e.g., `/profile/{userId}` is forbidden).
* **No `userId` in request body**.
* All access is **scoped to the authenticated user** extracted from the JWT.

---

## API Contract

### Authentication

* Requests must include: `Authorization: Bearer <JWT>`
* The service must validate:

    * **JWT signature**
    * **JWT expiration**
* If the token is missing, invalid, or expired → **`401 Unauthorized`**.

#### Dev token acquisition

Provide a development-friendly way to obtain a valid JWT:

* `POST /api/v1/auth/login`
* Simple credentials/users (e.g., in-memory)
* Returns a signed JWT with the user identifier claim (preferably `sub`)

### Endpoints

#### 1) Get my profile

* `GET /api/v1/profile`

**Responses**

* `200 OK` — profile exists
* `404 Not Found` — profile does not exist
* `401 Unauthorized` — missing/invalid/expired token

#### 2) Create my profile

* `POST /api/v1/profile`
* Creates the profile associated with the authenticated user.

**Responses**

* `201 Created` — created successfully
* `400 Bad Request` — validation errors
* `401 Unauthorized` — missing/invalid/expired token
* `409 Conflict` — profile already exists for this user (or unique constraint violation)

#### 3) Update my profile

* `PUT /api/v1/profile`
* Full replacement semantics (fields omitted by the client are treated as absent and handled according to your DTO rules).

**Responses**

* `200 OK` — updated successfully
* `400 Bad Request` — validation errors
* `401 Unauthorized` — missing/invalid/expired token
* `404 Not Found` — profile does not exist

---

## Data Model

### Entity: `UserProfile`

Associated 1:1 with the authenticated user identifier.

Suggested fields:

* `email` *(required, valid format, globally unique)*
* `firstName` *(required)*
* `lastName` *(required)*
* `birthDate` *(optional, must be in the past)*
* `phoneNumber` *(optional)*
* `address` *(optional object)*

    * `street`
    * `city`
    * `country`
    * `postalCode`
* `updatedAt` *(backend-managed)*

### Validation

* Use **Bean Validation** (`jakarta.validation`) on DTOs.
* Validate required fields and formats.
* Ensure email uniqueness across all profiles.

---

## Persistence

* Prefer **H2** for local execution.
* Use **Spring Data JPA** repositories.
* Enforce uniqueness at the DB level where applicable (e.g., email), and return **409** on conflicts.

---

## Error Handling

Provide consistent, predictable error responses:

* `400` for validation problems (include field-level errors)
* `401` for auth problems
* `404` when profile is missing
* `409` when attempting to create twice or when uniqueness is violated

Implement centralized exception handling (e.g., `@RestControllerAdvice`).

---

## Architecture Expectations

* Reasonable separation of concerns:

    * `controller` → `service` → `repository`
* DTOs for request/response (avoid exposing entities directly)
* Clean mapping between DTOs and entities

---

## Tests

Include tests that demonstrate:

* No token → **401**
* With token: create → **201**
* With token: get after create → **200**
* At least one of:

    * validation failure → **400**
    * duplicate create / conflict → **409**

---

## Deliverables

* Git repository containing the service implementation
* Updated `README.md` including:

    * how to run the service
    * how to obtain and use a token
    * example `curl` requests (with `Authorization: Bearer ...`)
    * key assumptions and tradeoffs

---

## Definition of Done

* All endpoints behave as specified (status codes + semantics)
* JWT validation is enforced on protected endpoints
* User identity is derived only from the token, with no client override
* Validation and error responses are consistent
* Tests cover the minimum scenarios listed above and pass in CI/local runs
