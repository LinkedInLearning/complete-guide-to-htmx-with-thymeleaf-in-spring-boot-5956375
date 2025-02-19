package com.mdrsolutions.records_management.controller.dto;

import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ErrorUtil {

    public static List<ObjectError> mapToObjectErrorList(Map<String, String> errorMap) {
        return errorMap.entrySet().stream()
                .map(entry -> new ObjectError(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}