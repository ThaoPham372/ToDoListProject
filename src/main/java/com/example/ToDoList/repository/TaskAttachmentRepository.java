package com.example.ToDoList.repository;

import com.example.ToDoList.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAttachmentRepository extends JpaRepository<TaskAttachment, String> {
    List<TaskAttachment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
}
