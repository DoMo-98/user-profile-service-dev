package com.example.userprofileservicedev.constants;

/**
 * Constants for internationalization (i18n) message keys.
 * These keys are resolved from the messages.properties file.
 */
public final class MessageKeys {

    private MessageKeys() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Validation Messages
    public static final String VALIDATION_EMAIL_REQUIRED = "validation.email.required";
    public static final String VALIDATION_EMAIL_INVALID = "validation.email.invalid";
    public static final String VALIDATION_FIRSTNAME_REQUIRED = "validation.firstName.required";
    public static final String VALIDATION_FIRSTNAME_INVALID = "validation.firstName.invalid";
    public static final String VALIDATION_LASTNAME_REQUIRED = "validation.lastName.required";
    public static final String VALIDATION_LASTNAME_INVALID = "validation.lastName.invalid";
    public static final String VALIDATION_BIRTHDATE_PAST = "validation.birthDate.past";
    public static final String VALIDATION_BIRTHDATE_REQUIRED = "validation.birthDate.required";
    public static final String VALIDATION_PHONENUMBER_INVALID = "validation.phoneNumber.invalid";
    public static final String VALIDATION_POSTALCODE_INVALID = "validation.postalCode.invalid";
    public static final String VALIDATION_USERNAME_REQUIRED = "validation.username.required";
    public static final String VALIDATION_ERROR = "validation.error";

    // Error Messages
    public static final String ERROR_PROFILE_NOT_FOUND = "error.profile.notfound";
    public static final String ERROR_PROFILE_CONFLICT = "error.profile.conflict";
    public static final String ERROR_EMAIL_CONFLICT = "error.email.conflict";
    public static final String ERROR_UNAUTHORIZED = "error.unauthorized";
}


