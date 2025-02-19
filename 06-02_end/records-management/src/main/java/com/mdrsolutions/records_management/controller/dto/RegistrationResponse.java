package com.mdrsolutions.records_management.controller.dto;

import org.springframework.validation.ObjectError;
import java.util.List;

public class RegistrationResponse {
    private boolean success;
    private String message;
    private List<ObjectError> errors;

    // Constructors, getters, and setters

    public RegistrationResponse() {}

    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RegistrationResponse(boolean success, String message, List<ObjectError> errors) {
        this.success = success;
        this.message = message;
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
