package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskListService taskListService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskListService taskListService) {
        this.taskRepository = taskRepository;
        this.taskListService = taskListService;
    }

    @Transactional
    public Task save(Task task, Long TaskListId) {
        var list = taskListService.getListById(TaskListId);
        if (list == null) {
            throw new EntityNotFoundException("TaskList");
        }
        task.setList(list);
        return taskRepository.save(task);
    }

    @Transactional
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task not found"));
    }


    @Transactional
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public void deleteTask(long taskId) {
        taskRepository.deleteById(taskId);
    }


    @Transactional
    public Task updateTask(Long id, Task task) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task taskToUpdate = taskOptional.get();
        }
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTaskName(Long taskId, String name) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        task.setName(name);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatusToCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        task.setStatus(Task.Status.STATUS_COMPLETED);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatusToInProgress(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        task.setStatus(Task.Status.STATUS_IN_PROGRESS);
        return taskRepository.save(task);
    }

}
