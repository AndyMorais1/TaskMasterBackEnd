package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.TaskMapper;
import com.taskmaster.taskmaster.dto.TaskCreateDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
import com.taskmaster.taskmaster.dto.TaskUpdateNameDTO;
import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tarefas", description = "Tudo relacionado a tarefas")
@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @Operation(summary = "Criar uma nova tarefa em uma lista")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO createDTO) {
        Task newTask = taskService.save(TaskMapper.toTask(createDTO), createDTO.getListId());
        return ResponseEntity.status(201).body(TaskMapper.toDTO(newTask));
    }


    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById( @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
       List<Task> tasksList= taskService.getAll();
       return ResponseEntity.ok(TaskMapper.toListDTO(tasksList));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> delete(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        taskService.deleteTask(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @PutMapping("/{taskId}/name")
    public ResponseEntity<TaskResponseDTO> updateName(@PathVariable Long taskId, @RequestBody TaskUpdateNameDTO body) {
        Task task = taskService.updateTaskName(taskId, body.getName());
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @PutMapping("/{taskId}/status/completed")
    public ResponseEntity<TaskResponseDTO> updateStatusToCompleted(@PathVariable Long taskId) {
        Task task = taskService.updateStatusToCompleted(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @PutMapping("/{taskId}/status/in-progress")
    public ResponseEntity<TaskResponseDTO> updateStatusToInProgress(@PathVariable Long taskId) {
        Task task = taskService.updateStatusToInProgress(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

}
