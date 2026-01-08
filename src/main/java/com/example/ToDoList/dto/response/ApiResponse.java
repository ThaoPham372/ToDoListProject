package com.example.ToDoList.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                "SUCCESS",
                message,
                data
        );
    }

    public static <T> ApiResponse<T> error(String code, String message, T data) {
        return new ApiResponse<>(
                false,
                code,
                message,
                data
        );
    }
}