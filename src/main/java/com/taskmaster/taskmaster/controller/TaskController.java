package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.TaskMapper;
import com.taskmaster.taskmaster.dto.TaskCreateDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
import com.taskmaster.taskmaster.dto.TaskUpdateNameDTO;
import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Criar uma nova tarefa em uma lista", description = "Cria uma tarefa associada a uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Sem permissão de escrita na lista", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista ou usuário não encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskCreateDTO createDTO, @RequestParam Long userId) {
        Task newTask = taskService.save(TaskMapper.toTask(createDTO), createDTO.getListId(), userId);
        return ResponseEntity.status(201).body(TaskMapper.toDTO(newTask));
    }


    @Operation(summary = "Buscar tarefa por ID", description = "Retorna os detalhes de uma tarefa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById( @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @Operation(summary = "Listar todas as tarefas", description = "Retorna todas as tarefas cadastradas no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<Task> tasksList= taskService.getAll();
        return ResponseEntity.ok(TaskMapper.toListDTO(tasksList));
    }

    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para deletar a tarefa", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> delete(@PathVariable Long taskId, @RequestParam Long userId) {
        Task task = taskService.getTaskById(taskId);
        taskService.deleteTask(taskId, userId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @Operation(summary = "Atualizar nome da tarefa", description = "Atualiza o título/nome de uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão de escrita na tarefa", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @PutMapping("/{taskId}/name")
    public ResponseEntity<TaskResponseDTO> updateName(@PathVariable Long taskId, @RequestBody TaskUpdateNameDTO body, @RequestParam Long userId) {
        Task task = taskService.updateTaskName(taskId, body.getName(), userId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @Operation(summary = "Marcar tarefa como concluída", description = "Atualiza o status da tarefa para COMPLETED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão de escrita na tarefa", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @PutMapping("/{taskId}/status/completed")
    public ResponseEntity<TaskResponseDTO> updateStatusToCompleted(@PathVariable Long taskId, @RequestParam Long userId) {
        Task task = taskService.updateStatusToCompleted(taskId, userId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

    @Operation(summary = "Marcar tarefa como em progresso", description = "Atualiza o status da tarefa para IN_PROGRESS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão de escrita na tarefa", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada", content = @Content)
    })
    @PutMapping("/{taskId}/status/in-progress")
    public ResponseEntity<TaskResponseDTO> updateStatusToInProgress(@PathVariable Long taskId, @RequestParam Long userId) {
        Task task = taskService.updateStatusToInProgress(taskId, userId);
        return ResponseEntity.status(200).body(TaskMapper.toDTO(task));
    }

}
