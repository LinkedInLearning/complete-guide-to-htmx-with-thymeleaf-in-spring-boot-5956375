package com.mdrsolutions.records_management.controller.dto;

import java.security.Principal;
import java.time.LocalDateTime;

public record ChatMessage(
        String styleClass,
        Principal principal,
        String message,
        LocalDateTime localDate
) {
}
