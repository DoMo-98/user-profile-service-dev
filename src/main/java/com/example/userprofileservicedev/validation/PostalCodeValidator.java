package com.example.userprofileservicedev.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Generic postal code validator for international formats.
 * Accepts alphanumeric characters, spaces, and hyphens.
 * Supports formats from multiple countries (US, UK, Canada, Spain, Germany, etc.)
 * Examples: 12345, 12345-6789, SW1A 1AA, M5H 2N2, 28001
 */
public class PostalCodeValidator implements ConstraintValidator<ValidPostalCode, String> {

    // Max length based on longest known postal codes (e.g., UK: SW1A 1AA = 8 chars)
    private static final int MAX_LENGTH = 10;
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-]+$");
    private static final Pattern CONSECUTIVE_SPACES = Pattern.compile("\\s{2,}");
    private static final Pattern CONSECUTIVE_HYPHENS = Pattern.compile("-{2,}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Use @NotBlank for null/empty validation
        }

        String trimmedValue = value.trim();

        // Check length
        if (trimmedValue.length() > MAX_LENGTH) {
            return false;
        }

        // Check basic pattern
        if (!POSTAL_CODE_PATTERN.matcher(trimmedValue).matches()) {
            return false;
        }

        // Reject multiple consecutive spaces or hyphens
        return !CONSECUTIVE_SPACES.matcher(trimmedValue).find()
               && !CONSECUTIVE_HYPHENS.matcher(trimmedValue).find();
    }
}

