package com.example.userprofileservicedev.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhoneNumberValidator using Google's libphonenumber library.
 */
class PhoneNumberValidatorTest {

    private PhoneNumberValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhoneNumberValidator();
    }

    @Test
    void whenPhoneNumberIsValid_thenValidationPasses() {
        // US numbers (real valid format)
        assertTrue(validator.isValid("+1 201 555 0123", null));
        assertTrue(validator.isValid("+12015550123", null));

        // UK numbers
        assertTrue(validator.isValid("+44 20 7946 0958", null));
        assertTrue(validator.isValid("+442079460958", null));

        // Spanish numbers
        assertTrue(validator.isValid("+34 612 345 678", null));
        assertTrue(validator.isValid("+34612345678", null));

        // Mexican numbers
        assertTrue(validator.isValid("+52 55 1234 5678", null));

        // Canadian numbers
        assertTrue(validator.isValid("+1 416 555 0199", null));
    }

    @Test
    void whenPhoneNumberContainsLetters_thenValidationFails() {
        assertFalse(validator.isValid("ABC-DEFG", null));
        assertFalse(validator.isValid("555-CALL", null));
        assertFalse(validator.isValid("55-55-55A", null));
        assertFalse(validator.isValid("phone123", null));
    }

    @Test
    void whenPhoneNumberContainsInvalidCharacters_thenValidationFails() {
        assertFalse(validator.isValid("phone#123", null));
        assertFalse(validator.isValid("555@1234", null));
        assertFalse(validator.isValid("555*1234", null));
    }

    @Test
    void whenPhoneNumberIsTooShort_thenValidationFails() {
        assertFalse(validator.isValid("+1 123", null));
        assertFalse(validator.isValid("+34 12", null));
    }

    @Test
    void whenPhoneNumberIsTooLong_thenValidationFails() {
        assertFalse(validator.isValid("+1 555 123 4567 890 123", null));
    }

    @Test
    void whenPhoneNumberHasInvalidFormat_thenValidationFails() {
        assertFalse(validator.isValid("555-1234", null)); // Missing country code
        assertFalse(validator.isValid("(123) 456-7890", null)); // Missing country code
        assertFalse(validator.isValid("5551234567", null)); // Missing country code
    }

    @Test
    void whenPhoneNumberIsNullOrEmpty_thenValidationPasses() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }
}

