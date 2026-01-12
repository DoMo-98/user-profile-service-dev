package com.example.userprofileservicedev.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for AlphabeticName annotation.
 * Allows letters (including accented characters), spaces, hyphens, and apostrophes.
 */
public class AlphabeticNameValidator implements ConstraintValidator<AlphabeticName, String> {

    // Pattern allows: letters (including Unicode), spaces, hyphens, and apostrophes
    private static final String NAME_PATTERN = "^[\\p{L}\\s'\\-]+$";

    @Override
    public void initialize(AlphabeticName constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        return value.matches(NAME_PATTERN);
    }
}

