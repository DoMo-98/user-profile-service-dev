package com.example.userprofileservicedev.validation;

import com.example.userprofileservicedev.constants.MessageKeys;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validates that a phone number contains only valid characters:
 * digits, spaces, hyphens, parentheses, and plus sign.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface ValidPhoneNumber {

    String message() default "{" + MessageKeys.VALIDATION_PHONENUMBER_INVALID + "}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

