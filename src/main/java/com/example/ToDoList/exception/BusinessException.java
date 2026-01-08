package com.example.ToDoList.exception;

public class BusinessException extends BaseException {
    // lỗi nghiệp vụ (service)
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
