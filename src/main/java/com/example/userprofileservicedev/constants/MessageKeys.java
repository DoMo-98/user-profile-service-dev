package com.example.userprofileservicedev.constants;

/**
 * Constantes para las claves de mensajes de internacionalización (i18n).
 * Estas claves se resuelven desde el archivo messages.properties.
 */
public final class MessageKeys {

    private MessageKeys() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada");
    }

    // Validation Messages
    public static final String VALIDATION_EMAIL_REQUIRED = "validation.email.required";
    public static final String VALIDATION_EMAIL_INVALID = "validation.email.invalid";
    public static final String VALIDATION_FIRSTNAME_REQUIRED = "validation.firstName.required";
    public static final String VALIDATION_LASTNAME_REQUIRED = "validation.lastName.required";
    public static final String VALIDATION_BIRTHDATE_PAST = "validation.birthDate.past";
    public static final String VALIDATION_USERNAME_REQUIRED = "validation.username.required";
    public static final String VALIDATION_ERROR = "validation.error";

    // Error Messages
    public static final String ERROR_PROFILE_NOT_FOUND = "error.profile.notfound";
    public static final String ERROR_PROFILE_CONFLICT = "error.profile.conflict";
    public static final String ERROR_EMAIL_CONFLICT = "error.email.conflict";
    public static final String ERROR_UNAUTHORIZED = "error.unauthorized";
}


