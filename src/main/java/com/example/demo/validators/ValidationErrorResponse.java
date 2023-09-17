package com.example.demo.validators;

public class ValidationErrorResponse {
    private String invalidField;
    private String validationMessage;

    public ValidationErrorResponse(String invalidField, String validationMessage) {
        this.invalidField = invalidField;
        this.validationMessage = validationMessage;
    }

    public String getInvalidField() {
        return invalidField;
    }

    public void setInvalidField(String invalidField) {
        this.invalidField = invalidField;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}
