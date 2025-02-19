package com.mdrsolutions.records_management.validation;

import com.mdrsolutions.records_management.entity.PersonType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Set;

public class PersonTypeValidator implements ConstraintValidator<ValidPersonType, String> {

    private static Logger LOGGER = LoggerFactory.getLogger(PersonTypeValidator.class);
    private final Set<String> validPersonTypes = EnumSet.allOf(PersonType.class)
            .stream()
            .map(Enum::name)
            .collect(java.util.stream.Collectors.toSet());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        LOGGER.info("isValid(...)");
        if (value == null || value.isBlank()) {
            LOGGER.warn("PersonTypeValidator.isValid(...) - Value is null or blank!");
            return false; // PersonType cannot be null
        }
        LOGGER.info("PersonType is valid");
        return validPersonTypes.contains(value);
    }
}
