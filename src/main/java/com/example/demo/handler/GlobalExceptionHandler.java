package com.example.demo.handler;

import com.example.demo.validators.ValidationErrorException;
import com.example.demo.validators.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationErrorException.class)
    public ResponseEntity<Object> handleValidationError(ValidationErrorException ex) {
        List<ValidationErrorResponse> validationErrors = ex.getValidationErrors();
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}
