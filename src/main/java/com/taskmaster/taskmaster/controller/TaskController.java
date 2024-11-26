package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.TaskMapper;
import com.taskmaster.taskmaster.dto.TaskCreateDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
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
    @PostMapping("/create/{listId}")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO createDTO, @PathVariable Long listId) {
        Task newTask = taskService.save(TaskMapper.toTask(createDTO), listId);
        return ResponseEntity.status(201).body(TaskMapper.toDTO(newTask));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById( @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @GetMapping("/lists")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
       List<Task> tasksList= taskService.getAll();
       return ResponseEntity.ok(TaskMapper.toListDTO(tasksList));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<TaskResponseDTO> delete(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        taskService.deleteTask(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @PostMapping("/updatename/{taskId}/{newName}")
    public ResponseEntity<TaskResponseDTO> updateName(@PathVariable Long taskId, @PathVariable String newName) {
        Task task = taskService.updateTaskName(taskId,newName);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }


}
