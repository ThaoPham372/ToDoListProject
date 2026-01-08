package com.example.ToDoList.service;

import com.example.ToDoList.dto.request.CreateTaskRequest;
import com.example.ToDoList.dto.request.UpdateTaskPartialRequest;
import com.example.ToDoList.dto.request.UpdateTaskRequest;
import com.example.ToDoList.dto.response.TaskResponse;
import com.example.ToDoList.entity.Task;
import com.example.ToDoList.entity.TaskStatus;
import com.example.ToDoList.exception.BusinessException;
import com.example.ToDoList.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    // Create task
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
    // Get all task
    public List<TaskResponse> getAllTask() {
        try {
            return taskRepository.findByStatus(TaskStatus.ACTIVE)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        }catch (Exception e) {
            throw new BusinessException(
                    "GET_TASKS_FAILED",
                    "Failed to get task list"
            );
        }
    }
    // Get task by ID
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findByIdAndStatus(id, TaskStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(
                        "TASK_NOT_FOUND",
                        "Task not found with id : " + id
                ));
        return mapToResponse(task);
    }

    // Update Task By Id Function
    public TaskResponse updateTask (Long id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "TASK_NOT_FOUND",
                        "Task not found with id : " + id
                ));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());

        Task updated = taskRepository.save(task);

        return mapToResponse(updated);
    }

    // Update Task Partial
    public TaskResponse patchTask(Long id, UpdateTaskPartialRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "TASK_NOT_FOUND",
                        "Task not found with id: " + id
                ));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getCompleted() != null) {
            task.setCompleted(request.getCompleted());
        }

        Task updated = taskRepository.save(task);

        return mapToResponse(updated);
    }

    // Delete
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "TASK_NOT_FOUND",
                        "Task not found with id: " + id
                ));

        if (task.getStatus() == TaskStatus.DELETED) {
            throw new BusinessException(
                    "TASK_ALREADY_DELETED",
                    "Task already deleted"
            );
        }

        task.setStatus(TaskStatus.DELETED);

        taskRepository.save(task);
    }

    // Mapping function
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .build();
    }

}
