package com.mdrsolutions.records_management.controller.dto;

import com.mdrsolutions.records_management.entity.PriorSchool;
import com.mdrsolutions.records_management.entity.Sex;

public record StudentDto(Long studentId,
                         Long personId,
                         String firstName,
                         String lastName,
                         String middleName,
                         Sex sex,
                         String age,
                         String gradeLevel,
                         MissingDetailsDto missingDetailsDto
) {
}
