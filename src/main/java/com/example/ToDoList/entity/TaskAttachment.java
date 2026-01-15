package com.example.ToDoList.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskAttachment {

    @Id
    @Column(length = 36)
    private String id; // UUID string

    @NotNull(message = "Task id must not be null")
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @NotBlank(message = "Original name must not be blank")
    @Size(max = 255, message = "Original name must be at most 255 characters")
    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @NotBlank(message = "S3 key must not be blank")
    @Size(max = 1024, message = "S3 key must be at most 1024 characters")
    @Column(name = "s3_key", nullable = false, length = 1024)
    private String s3Key;

    @Size(max = 255, message = "Content type must be at most 255 characters")
    @Column(name = "content_type", length = 255)
    private String contentType;

    @Column(nullable = false)
    private long size;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }
}
