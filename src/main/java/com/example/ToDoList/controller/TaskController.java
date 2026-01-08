package com.example.ToDoList.controller;

import com.example.ToDoList.dto.request.CreateTaskRequest;
import com.example.ToDoList.dto.request.UpdateTaskPartialRequest;
import com.example.ToDoList.dto.request.UpdateTaskRequest;
import com.example.ToDoList.dto.response.ApiResponse;
import com.example.ToDoList.dto.response.TaskResponse;
import com.example.ToDoList.exception.ValidationException;
import com.example.ToDoList.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            throw new ValidationException(errors);
        }

        TaskResponse data = taskService.createTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTask(){
        List<TaskResponse> data = taskService.getAllTask();

        return ResponseEntity
                .ok(ApiResponse.success("Get task list successfully", data));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id
    ) {
        TaskResponse data = taskService.getTaskById(id);

        return ResponseEntity
                .ok(ApiResponse.success("Get task successfully", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            throw new ValidationException(errors);
        }

        TaskResponse data = taskService.updateTask(id, request);

        return ResponseEntity
                .ok(ApiResponse.success("Task updated successfully", data));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> patchTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskPartialRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            throw new ValidationException(errors);
        }

        TaskResponse data = taskService.patchTask(id, request);

        return ResponseEntity
                .ok(ApiResponse.success("Task updated partially", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);

        return ResponseEntity
                .ok(ApiResponse.success("Task deleted successfully", null));
    }

}
