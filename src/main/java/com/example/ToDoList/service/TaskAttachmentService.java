package com.example.ToDoList.service;

import com.example.ToDoList.dto.response.AttachmentResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface TaskAttachmentService {
    AttachmentResponse upload(Long taskId, MultipartFile file) throws IOException;
    List<AttachmentResponse> list(Long taskId);
    String signedUrl(Long taskId, String attachmentId);
    void delete(Long taskId, String attachmentId);
}
