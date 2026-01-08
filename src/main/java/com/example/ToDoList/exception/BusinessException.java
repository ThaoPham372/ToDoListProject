package com.example.ToDoList.exception;

public class BusinessException extends BaseException {
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
