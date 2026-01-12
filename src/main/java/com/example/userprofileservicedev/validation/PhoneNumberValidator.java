package com.example.userprofileservicedev.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for ValidPhoneNumber annotation.
 * Allows digits, spaces, hyphens, parentheses, and plus sign.
 */
public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    // Pattern allows: digits, spaces, hyphens, parentheses, and plus sign
    // Examples: +1 (555) 123-4567, +34 612 345 678, 555-1234
    private static final String PHONE_PATTERN = "^[\\d\\s\\-()+ ]+$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Null or empty values are considered valid (use @NotBlank/@NotNull for required fields)
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // Check if the value matches the pattern
        return value.matches(PHONE_PATTERN);
    }
}

