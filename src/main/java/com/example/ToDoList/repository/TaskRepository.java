package com.example.ToDoList.repository;

import com.example.ToDoList.entity.Task;
import com.example.ToDoList.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    Optional<Task> findByIdAndStatus(Long id, TaskStatus status);
}
