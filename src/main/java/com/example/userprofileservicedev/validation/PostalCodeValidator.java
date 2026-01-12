package com.example.userprofileservicedev.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for ValidPostalCode annotation.
 * Allows alphanumeric characters, spaces, and hyphens.
 */
public class PostalCodeValidator implements ConstraintValidator<ValidPostalCode, String> {

    // Pattern allows: alphanumeric characters, spaces, and hyphens
    // Examples: 12345, 12345-6789, SW1A 1AA, 28001
    private static final String POSTAL_CODE_PATTERN = "^[a-zA-Z0-9\\s\\-]+$";

    @Override
    public void initialize(ValidPostalCode constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return value.matches(POSTAL_CODE_PATTERN);
    }
}

