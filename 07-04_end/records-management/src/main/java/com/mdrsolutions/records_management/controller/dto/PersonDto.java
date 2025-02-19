package com.mdrsolutions.records_management.controller.dto;

import com.mdrsolutions.records_management.entity.EmploymentStatus;
import com.mdrsolutions.records_management.entity.LegalGuardianType;
import com.mdrsolutions.records_management.entity.PersonType;

import java.util.Set;

/**
 * A Record-based Data Transfer Object (DTO) for the Person entity.
 *
 * Fields are immutable and defined in the record header.
 * The generated constructor sets them all.
 */
public record PersonDto(
        Long personId,
        EmploymentStatus employmentStatus,
        String prefix,
        String firstName,
        String middleName,
        String lastName,
        String suffix,
        PersonType personType,
        LegalGuardianType legalGuardianType,
        Long userId,
        EmailDto emailDto
) {}