package com.example.userprofileservicedev.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AlphabeticNameValidator.
 */
class AlphabeticNameValidatorTest {

    private AlphabeticNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AlphabeticNameValidator();
    }

    @Test
    void whenNameIsValid_thenValidationPasses() {
        assertTrue(validator.isValid("John", null));
        assertTrue(validator.isValid("Mary-Jane", null));
        assertTrue(validator.isValid("O'Connor", null));
        assertTrue(validator.isValid("José García", null));
        assertTrue(validator.isValid("María José", null));
        assertTrue(validator.isValid("García-López", null));
        assertTrue(validator.isValid("De la Cruz", null));
    }

    @Test
    void whenNameContainsNumbers_thenValidationFails() {
        assertFalse(validator.isValid("John123", null));
        assertFalse(validator.isValid("User99", null));
        assertFalse(validator.isValid("Test1", null));
    }

    @Test
    void whenNameContainsSpecialCharacters_thenValidationFails() {
        assertFalse(validator.isValid("User@123", null));
        assertFalse(validator.isValid("Test_User", null));
        assertFalse(validator.isValid("User#1", null));
        assertFalse(validator.isValid("Name$", null));
    }

    @Test
    void whenNameIsNullOrEmpty_thenValidationPasses() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
        assertTrue(validator.isValid("   ", null));
    }
}

