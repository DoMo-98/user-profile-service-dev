package com.example.userprofileservicedev.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a string contains only alphabetic characters, spaces, hyphens, and apostrophes.
 * This is useful for validating names (first name, last name).
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AlphabeticNameValidator.class)
@Documented
public @interface AlphabeticName {

    String message() default "Must contain only letters, spaces, hyphens, and apostrophes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

