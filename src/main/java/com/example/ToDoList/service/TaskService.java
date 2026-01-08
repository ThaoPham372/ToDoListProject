package com.example.ToDoList.service;

import com.example.ToDoList.dto.request.CreateTaskRequest;
import com.example.ToDoList.dto.response.TaskResponse;
import com.example.ToDoList.entity.Task;
import com.example.ToDoList.exception.BusinessException;
import com.example.ToDoList.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse createTask(CreateTaskRequest request) {
        try {
            Task task = Task.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .build();

            Task saved = taskRepository.save(task);

            return TaskResponse.builder()
                    .id(saved.getId())
                    .title(saved.getTitle())
                    .description(saved.getDescription())
                    .completed(saved.isCompleted())
                    .createdAt(saved.getCreatedAt())
                    .build();

        } catch (Exception e) {
            // Không trả ApiResponse ở service nữa, service throw để handler xử lý tập trung
            throw new BusinessException("CREATE_TASK_FAILED", "Failed to create task");
        }
    }
}
