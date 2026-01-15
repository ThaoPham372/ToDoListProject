package com.example.ToDoList.service.s3;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Template s3Template;

    @Value("${app.aws.s3.bucket}")
    private String bucket;

    public String upload(String key, MultipartFile file) throws IOException {
        // metadata tối thiểu
        var metadata = io.awspring.cloud.s3.ObjectMetadata.builder()
                .contentType(file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType())
                .build();

        try (InputStream is = file.getInputStream()) {
            // upload vào S3
            s3Template.upload(bucket, key, is, metadata);
        }
        return key;
    }

    public URL createSignedGetUrl(String key, Duration ttl) {
        return s3Template.createSignedGetURL(bucket, key, ttl);
    }

    public void delete(String key) {
        s3Template.deleteObject(bucket, key);
    }

    public static String safeFileName(String original) {
        if (original == null || original.isBlank()) return "file";
        return original.replaceAll("\\s+", "_");
    }
}