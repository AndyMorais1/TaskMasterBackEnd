package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.Permission;
import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Task save(Task task, Long taskListId, Long userId) {
        if (taskListId != null) {
            var list = taskListService.getListById(taskListId);
            if (list == null) {
                throw new EntityNotFoundException("TaskList not found");
            }
            // Check permission
            if (!taskListService.hasAccess(taskListId, userId, Permission.WRITE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add tasks to this list");
            }
            task.setList(list); // Associa a lista à tarefa
        } else {
            task.setList(null); // Caso não haja lista, setamos null ou uma "Lista Geral"
        }

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
    public void deleteTask(long taskId, Long userId) {
        Task task = getTaskById(taskId);
        if (task.getList() != null) {
            if (!taskListService.hasAccess(task.getList().getId(), userId, Permission.WRITE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete tasks from this list");
            }
        }
        taskRepository.deleteById(taskId);
    }


    @Transactional
    public Task updateTaskName(Long taskId, String name, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        if (task.getList() != null) {
            if (!taskListService.hasAccess(task.getList().getId(), userId, Permission.WRITE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update tasks in this list");
            }
        }
        task.setName(name);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatusToCompleted(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        if (task.getList() != null) {
            if (!taskListService.hasAccess(task.getList().getId(), userId, Permission.WRITE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update tasks in this list");
            }
        }
        task.setStatus(Task.Status.STATUS_COMPLETED);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateStatusToInProgress(Long id, Long userId) {
        Task task = taskRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Task not found")
        );
        if (task.getList() != null) {
            if (!taskListService.hasAccess(task.getList().getId(), userId, Permission.WRITE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update tasks in this list");
            }
        }
        task.setStatus(Task.Status.STATUS_IN_PROGRESS);
        return taskRepository.save(task);
    }

}
