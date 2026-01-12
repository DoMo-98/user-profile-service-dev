package com.example.userprofileservicedev.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PostalCodeValidator.
 */
class PostalCodeValidatorTest {

    private PostalCodeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PostalCodeValidator();
    }

    @Test
    void whenPostalCodeIsValid_thenValidationPasses() {
        assertTrue(validator.isValid("12345", null));
        assertTrue(validator.isValid("12345-6789", null));
        assertTrue(validator.isValid("SW1A 1AA", null));
        assertTrue(validator.isValid("28001", null));
        assertTrue(validator.isValid("AB1 2CD", null));
        assertTrue(validator.isValid("H2X 3Y7", null));
    }

    @Test
    void whenPostalCodeContainsInvalidCharacters_thenValidationFails() {
        assertFalse(validator.isValid("!!!###", null));
        assertFalse(validator.isValid("12@34", null));
        assertFalse(validator.isValid("AB#CD", null));
        assertFalse(validator.isValid("12_345", null));
        assertFalse(validator.isValid("12*34", null));
    }

    @Test
    void whenPostalCodeIsNullOrEmpty_thenValidationPasses() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }
}

