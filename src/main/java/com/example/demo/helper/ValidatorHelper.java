package com.example.demo.helper;

public class ValidatorHelper {

    public static boolean fieldIsEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }

    public static boolean fieldContainsInvalidCharacters(String field) {
        return !field.matches("^[a-zA-Z\\s]+$");
    }
}
