package com.example.ToDoList.exception;

import java.util.Map;

public class ValidationException extends BaseException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("VALIDATION_FAILED", "Invalid input data");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
