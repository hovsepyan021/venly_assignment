package com.example.demo.validators;

import com.example.demo.helper.ValidatorHelper;
import com.example.demo.requests.AddWordRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class AddWordValidator implements ConstraintValidator<ValidAddWord, AddWordRequest> {
    @Override
    public void initialize(ValidAddWord constraintAnnotation) {
    }

    @Override
    public boolean isValid(AddWordRequest value, ConstraintValidatorContext context) {
        List<ValidationErrorResponse> validationErrors = new ArrayList<>();

        validateField("word1", value.getWord1(), validationErrors);
        validateField("word2", value.getWord2(), validationErrors);
        validateField("relation", value.getRelation(), validationErrors);

        if (!validationErrors.isEmpty()) {
            throw new ValidationErrorException(validationErrors);
        }

        return true;
    }

    private static void validateField(String fieldName, String value, List<ValidationErrorResponse> validationErrors) {
        if (ValidatorHelper.fieldIsEmpty(value)) {
            validationErrors.add(new ValidationErrorResponse(fieldName, "field is empty"));
        } else {
            if (ValidatorHelper.fieldContainsInvalidCharacters(value)) {
                validationErrors.add(new ValidationErrorResponse(fieldName, "field contains invalid characters"));
            }
        }
    }
}

