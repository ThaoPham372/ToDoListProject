package com.example.ToDoList.dto.response;
import java.time.LocalDateTime;

public record AttachmentResponse(
        String id,
        Long taskId,
        String originalName,
        String s3Key,
        String contentType,
        long size,
        LocalDateTime createdAt
) {}
