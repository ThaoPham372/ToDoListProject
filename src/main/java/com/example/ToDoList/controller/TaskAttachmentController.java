package com.example.ToDoList.controller;

import com.example.ToDoList.dto.response.ApiResponse;
import com.example.ToDoList.dto.response.AttachmentResponse;
import com.example.ToDoList.service.TaskAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;

    // POST /api/tasks/{id}/attachments
    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AttachmentResponse>> upload(
            @PathVariable("id") Long taskId,
            @RequestPart("file") MultipartFile file
    ) throws IOException {

        AttachmentResponse res = taskAttachmentService.upload(taskId, file);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Uploaded", res));
    }

    // GET /api/tasks/{id}/attachments
    @GetMapping("/{id}/attachments")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> list(@PathVariable("id") Long taskId) {

        List<AttachmentResponse> res = taskAttachmentService.list(taskId);

        return ResponseEntity.ok(ApiResponse.success("OK", res));
    }

    // GET /api/tasks/{id}/attachments/{attachmentId}/signed-url
    @GetMapping("/{id}/attachments/{attachmentId}/signed-url")
    public ResponseEntity<ApiResponse<String>> signedUrl(
            @PathVariable("id") Long taskId,
            @PathVariable String attachmentId
    ) {

        String url = taskAttachmentService.signedUrl(taskId, attachmentId);

        return ResponseEntity.ok(ApiResponse.success("OK", url));
    }

    // DELETE /api/tasks/{id}/attachments/{attachmentId}
    @DeleteMapping("/{id}/attachments/{attachmentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable("id") Long taskId,
            @PathVariable String attachmentId
    ) {

        taskAttachmentService.delete(taskId, attachmentId);

        return ResponseEntity.ok(ApiResponse.success("Deleted", null));
    }
}
