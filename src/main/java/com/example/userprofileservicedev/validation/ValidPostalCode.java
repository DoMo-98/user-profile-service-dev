package com.example.userprofileservicedev.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a postal code is alphanumeric and may contain spaces or hyphens.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostalCodeValidator.class)
@Documented
public @interface ValidPostalCode {

    String message() default "Must be alphanumeric and may contain spaces or hyphens";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

