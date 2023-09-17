package com.example.demo.validators;

import java.util.List;

public class ValidationErrorException extends RuntimeException {
    private final List<ValidationErrorResponse> validationErrors;

    public ValidationErrorException(List<ValidationErrorResponse> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<ValidationErrorResponse> getValidationErrors() {
        return validationErrors;
    }
}
