package com.example.ToDoList.service;

import com.example.ToDoList.dto.request.CreateTaskRequest;
import com.example.ToDoList.dto.response.ApiResponse;
import com.example.ToDoList.dto.response.TaskResponse;
import com.example.ToDoList.entity.Task;
import com.example.ToDoList.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public ApiResponse<TaskResponse> createTask(CreateTaskRequest request) {
        try {
            Task task = Task.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .build();

            Task savedTask = taskRepository.save(task);

            TaskResponse response = TaskResponse.builder()
                    .id(savedTask.getId())
                    .title(savedTask.getTitle())
                    .description(savedTask.getDescription())
                    .completed(savedTask.isCompleted())
                    .createdAt(savedTask.getCreatedAt())
                    .build();

            return ApiResponse.success(response);

        } catch (Exception e) {
            return ApiResponse.error("CREATE_TASK_FAILED", null);
        }
    }
}
