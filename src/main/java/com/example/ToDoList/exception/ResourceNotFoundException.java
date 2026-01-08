package com.example.ToDoList.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
