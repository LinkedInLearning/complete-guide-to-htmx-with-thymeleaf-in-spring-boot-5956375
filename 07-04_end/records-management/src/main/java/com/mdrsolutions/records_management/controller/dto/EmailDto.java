package com.mdrsolutions.records_management.controller.dto;

import com.mdrsolutions.records_management.entity.EmailType;

public record EmailDto (
        Long emailId,
        String emailAddress,
        EmailType emailType,
        Long personId
){}
