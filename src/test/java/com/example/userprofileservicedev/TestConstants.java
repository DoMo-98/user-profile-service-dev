package com.example.userprofileservicedev;

import java.time.LocalDate;

/**
 * Constantes para los tests de la aplicación.
 * Centraliza todos los literales para mejorar la mantenibilidad.
 */
public final class TestConstants {

    private TestConstants() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }

    // ===================== API Paths =====================
    public static final String API_V1_PROFILE = "/api/v1/profile";
    public static final String API_V1_AUTH_LOGIN = "/api/v1/auth/login";

    // ===================== HTTP Headers =====================
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // ===================== JSON Paths =====================
    public static final String JSON_PATH_STATUS = "$.status";
    public static final String JSON_PATH_MESSAGE = "$.message";
    public static final String JSON_PATH_PATH = "$.path";
    public static final String JSON_PATH_FIELD_ERRORS = "$.fieldErrors";
    public static final String JSON_PATH_ACCESS_TOKEN = "$.access_token";
    public static final String JSON_PATH_TOKEN_TYPE = "$.token_type";
    public static final String JSON_PATH_EXPIRES_IN = "$.expires_in_seconds";
    public static final String JSON_PATH_EMAIL = "$.email";
    public static final String JSON_PATH_FIRST_NAME = "$.firstName";
    public static final String JSON_PATH_LAST_NAME = "$.lastName";
    public static final String JSON_PATH_PHONE_NUMBER = "$.phoneNumber";
    public static final String ACCESS_TOKEN_FIELD = "access_token";

    // ===================== HTTP Status Codes =====================
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_CONFLICT = 409;

    // ===================== Token Response Values =====================
    public static final String TOKEN_TYPE_BEARER = "Bearer";
    public static final int TOKEN_EXPIRES_IN_SECONDS = 1800;

    // ===================== Error Messages =====================
    public static final String MSG_UNAUTHORIZED = "Authentication is required to access this resource";
    public static final String MSG_PROFILE_NOT_FOUND = "Profile not found";
    public static final String MSG_PROFILE_CONFLICT = "Profile already exists for this user";
    public static final String MSG_VALIDATION_ERROR = "Validation error";

    // ===================== Test Usernames =====================
    public static final String USERNAME_NOT_FOUND = "notfounduser";
    public static final String USERNAME_CONFLICT = "conflictuser";
    public static final String USERNAME_TEST = "testuser";
    public static final String USERNAME_VALIDATOR = "validator";
    public static final String USERNAME_USER1 = "user1";
    public static final String USERNAME_USER2 = "user2";
    public static final String USERNAME_USER3 = "user3";
    public static final String USERNAME_USER4 = "user4";
    public static final String USERNAME_USER5 = "user5";
    public static final String USERNAME_USER6 = "user6";

    // ===================== Test User Data =====================
    public static final String TEST_USER_ID = "id";
    public static final String FIRST_NAME_CONFLICT = "Conflict";
    public static final String FIRST_NAME_JOHN = "John";
    public static final String FIRST_NAME_JANE = "Jane";
    public static final String FIRST_NAME_ALICE = "Alice";
    public static final String FIRST_NAME_BOB = "Bob";
    public static final String FIRST_NAME_CHARLIE = "Charlie";
    public static final String FIRST_NAME_CHARLIE_UPDATED = "Charlie Updated";
    public static final String FIRST_NAME_TEST = "Test";
    public static final String FIRST_NAME_NON = "Non";

    public static final String LAST_NAME_USER = "User";
    public static final String LAST_NAME_DOE = "Doe";
    public static final String LAST_NAME_SMITH = "Smith";
    public static final String LAST_NAME_BROWN = "Brown";
    public static final String LAST_NAME_UPDATED = "Updated";
    public static final String LAST_NAME_EXISTENT = "Existent";

    // ===================== Test Emails =====================
    public static final String EMAIL_CONFLICT = "conflict@example.com";
    public static final String EMAIL_USER1 = "user1@example.com";
    public static final String EMAIL_USER2 = "user2@example.com";
    public static final String EMAIL_ALICE = "alice@example.com";
    public static final String EMAIL_BOB = "bob@example.com";
    public static final String EMAIL_CHARLIE = "charlie@example.com";
    public static final String EMAIL_CHARLIE_NEW = "charlie.new@example.com";
    public static final String EMAIL_TEST = "test@example.com";
    public static final String EMAIL_NON = "non@example.com";
    public static final String EMAIL_INVALID = "invalid-email";

    // ===================== Test Phone Numbers =====================
    public static final String PHONE_NUMBER_DEFAULT = "123456789";

    // ===================== Test Birth Dates =====================
    public static final LocalDate BIRTH_DATE_1990 = LocalDate.of(1990, 1, 1);
    public static final LocalDate BIRTH_DATE_1992 = LocalDate.of(1992, 2, 2);
    public static final LocalDate BIRTH_DATE_1985 = LocalDate.of(1985, 5, 5);
    public static final LocalDate BIRTH_DATE_1980 = LocalDate.of(1980, 8, 8);
}

