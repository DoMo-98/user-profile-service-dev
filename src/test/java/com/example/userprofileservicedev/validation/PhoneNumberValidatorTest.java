package com.example.userprofileservicedev.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhoneNumberValidator.
 */
class PhoneNumberValidatorTest {

    private PhoneNumberValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PhoneNumberValidator();
    }

    @Test
    void whenPhoneNumberIsValid_thenValidationPasses() {
        assertTrue(validator.isValid("+1 (555) 123-4567", null));
        assertTrue(validator.isValid("555-1234", null));
        assertTrue(validator.isValid("+34 612 345 678", null));
        assertTrue(validator.isValid("5551234567", null));
        assertTrue(validator.isValid("+44 20 7946 0958", null));
        assertTrue(validator.isValid("(123) 456-7890", null));
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
        assertFalse(validator.isValid("555_1234", null));
        assertFalse(validator.isValid("555*1234", null));
    }

    @Test
    void whenPhoneNumberIsNullOrEmpty_thenValidationPasses() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }
}

