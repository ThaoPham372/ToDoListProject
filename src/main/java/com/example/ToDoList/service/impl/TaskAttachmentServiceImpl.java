package com.example.ToDoList.service.impl;

import com.example.ToDoList.dto.response.AttachmentResponse;
import com.example.ToDoList.entity.Task;
import com.example.ToDoList.entity.TaskAttachment;
import com.example.ToDoList.entity.TaskStatus;
import com.example.ToDoList.exception.BusinessException;
import com.example.ToDoList.repository.TaskAttachmentRepository;
import com.example.ToDoList.repository.TaskRepository;
import com.example.ToDoList.service.TaskAttachmentService;
import com.example.ToDoList.service.s3.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final TaskRepository taskRepository;
    private final TaskAttachmentRepository attachmentRepository;
    private final S3StorageService s3StorageService;

    @Override
    public AttachmentResponse upload(Long taskId, MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                throw new BusinessException("FILE_EMPTY", "File is empty");
            }

            requireActiveTask(taskId);

            TaskAttachment att = TaskAttachment.builder()
                    .taskId(taskId)
                    .originalName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .s3Key("PENDING")
                    .build();

            TaskAttachment saved = attachmentRepository.save(att);

            String key = "tasks/" + taskId + "/"
                    + saved.getId() + "_"
                    + S3StorageService.safeFileName(file.getOriginalFilename());

            s3StorageService.upload(key, file);

            saved.setS3Key(key);
            TaskAttachment updated = attachmentRepository.save(saved);

            return mapToResponse(updated);

        } catch (BusinessException e) {
            throw e;

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(
                    "UPLOAD_ATTACHMENT_FAILED",
                    e.getMessage() != null ? e.getMessage() : "Failed to upload attachment"
            );
        }
    }

    @Override
    public List<AttachmentResponse> list(Long taskId) {
        try {
            requireActiveTask(taskId);

            return attachmentRepository.findByTaskIdOrderByCreatedAtDesc(taskId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("GET_ATTACHMENTS_FAILED", "Failed to get attachments");
        }
    }

    @Override
    public String signedUrl(Long taskId, String attachmentId) {
        try {
            requireActiveTask(taskId);

            TaskAttachment att = requireAttachmentBelongsToTask(taskId, attachmentId);

            return s3StorageService
                    .createSignedGetUrl(att.getS3Key(), Duration.ofMinutes(5))
                    .toString();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("GET_SIGNED_URL_FAILED", "Failed to create signed url");
        }
    }

    @Override
    public void delete(Long taskId, String attachmentId) {
        try {
            requireActiveTask(taskId);

            TaskAttachment att = requireAttachmentBelongsToTask(taskId, attachmentId);

            s3StorageService.delete(att.getS3Key());
            attachmentRepository.delete(att);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("DELETE_ATTACHMENT_FAILED", "Failed to delete attachment");
        }
    }

    // Common Function

    private Task requireActiveTask(Long taskId) {
        return taskRepository.findByIdAndStatus(taskId, TaskStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        "TASK_NOT_FOUND",
                        "Task not found with id: " + taskId
                ));
    }

    private TaskAttachment requireAttachmentBelongsToTask(Long taskId, String attachmentId) {
        TaskAttachment att = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new BusinessException(
                        "ATTACHMENT_NOT_FOUND",
                        "Attachment not found with id: " + attachmentId
                ));

        if (!att.getTaskId().equals(taskId)) {
            throw new BusinessException(
                    "ATTACHMENT_NOT_BELONG_TO_TASK",
                    "Attachment does not belong to task id: " + taskId
            );
        }
        return att;
    }

    private AttachmentResponse mapToResponse(TaskAttachment a) {
        return new AttachmentResponse(
                a.getId(),
                a.getTaskId(),
                a.getOriginalName(),
                a.getS3Key(),
                a.getContentType(),
                a.getSize(),
                a.getCreatedAt()
        );
    }
}
