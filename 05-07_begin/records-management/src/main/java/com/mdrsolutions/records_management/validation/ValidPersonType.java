package com.mdrsolutions.records_management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PersonTypeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonType {
    String message() default "Invalid person type"; // Default error message

    Class<?>[] groups() default {}; // Required parameter for groups of constraints

    Class<? extends Payload>[] payload() default {}; // Required parameter for payload
}
