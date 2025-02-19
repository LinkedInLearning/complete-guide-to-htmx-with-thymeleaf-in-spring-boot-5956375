package com.mdrsolutions.records_management.controller.dto;

import java.math.BigDecimal;

public record PriorSchoolDto(Long studentId,
                             Long priorSchoolId,
                             String schoolName,
                             String address,
                             String city,
                             String state,
                             String zip,
                             String phoneNumber,
                             String administratorFirstName,
                             String administratorLastName,
                             BigDecimal gpa,
                             String gradeLevel,
                             String dateStartedAttending,
                             String dateLastAttended) {
}
