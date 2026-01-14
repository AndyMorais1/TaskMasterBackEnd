package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.CollaboratorResponseDTO;
import com.taskmaster.taskmaster.dto.Mapper.TaskListMapper;
import com.taskmaster.taskmaster.dto.Mapper.TaskMapper;
import com.taskmaster.taskmaster.dto.ShareTaskListDTO;
import com.taskmaster.taskmaster.dto.TaskListCreateDTO;
import com.taskmaster.taskmaster.dto.TaskListResponseDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.model.TaskList;
import com.taskmaster.taskmaster.service.TaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task List", description = "Tudo relacionado a listas de tarefas")
@RestController
@RequestMapping("api/v1/task-lists")
public class TaskListController {

    @Autowired
    private final TaskListService taskListService;

    @Autowired
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @Operation(summary = "Criar uma nova lista de tarefas para um usuario", description = "Cria uma nova lista de tarefas associada a um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lista criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskListResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Lista com este nome já existe para o usuário", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskListResponseDTO> create(
            @RequestBody @Valid TaskListCreateDTO createDTO,
            @RequestParam Long userid) {

        // Converte o DTO para a entidade TaskList
        TaskList taskList = TaskListMapper.toTaskList(createDTO);

        // Salva a lista, verificando a existência
        TaskList savedTaskList = taskListService.save(taskList, userid);

        // Converte a entidade salva de volta para o DTO de resposta
        TaskListResponseDTO responseDTO = TaskListMapper.toDTO(savedTaskList);

        // Retorna uma resposta HTTP com status CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }


    @Operation(summary = "Buscar lista por ID", description = "Retorna os detalhes de uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskListResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado à lista", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> searchById(@PathVariable Long id, @RequestParam Long userId) {
        TaskList list1 = taskListService.getListById(id, userId);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
    }

    @Operation(summary = "Buscar lista por nome", description = "Retorna uma lista pelo nome e ID do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskListResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @GetMapping("/by-name/{listName}")
    public ResponseEntity<TaskListResponseDTO> searchByListName(@PathVariable String listName, @RequestParam Long userId) {
        TaskList list1 = taskListService.getListByName(listName, userId);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
    }

    @Operation(summary = "Listar listas do usuário", description = "Retorna todas as listas que o usuário possui ou tem acesso compartilhado")
    @ApiResponse(responseCode = "200", description = "Listas retornadas com sucesso")
    @GetMapping
    public ResponseEntity<List<TaskListResponseDTO>> listLists(@RequestParam Long userId) {
        List<TaskList> TaskListsList = taskListService.getTaskListByUserId(userId);
        return ResponseEntity.ok(TaskListMapper.toListDTO(TaskListsList));
    }

    @Operation(summary = "Deletar lista", description = "Remove uma lista de tarefas (apenas o dono pode fazer isso)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista deletada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Apenas o dono pode deletar a lista", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @DeleteMapping("/{listId}")
    public ResponseEntity<TaskListResponseDTO> delete(@PathVariable Long listId, @RequestParam Long userId) {
        TaskList list1 = taskListService.getListById(listId);
        taskListService.deleteList(listId, userId);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
    }

    @Operation(summary = "Listar tarefas de uma lista", description = "Retorna todas as tarefas de uma lista específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefas retornadas com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado à lista", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @GetMapping("/{listId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@PathVariable Long listId, @RequestParam Long userId)
    {
        List<Task> tasks = taskListService.getTasks( listId, userId);
        List<TaskResponseDTO> dtoTasks = TaskMapper.toListDTO(tasks);
        return ResponseEntity.status(200).body(dtoTasks);
    }


    @Operation(summary = "Atualizar nome da lista", description = "Atualiza o nome de uma lista de tarefas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão de escrita na lista", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @PutMapping("/{tasklistId}/name")
    public ResponseEntity<TaskListResponseDTO> updateTaskListName(@PathVariable Long tasklistId, @RequestBody TaskListCreateDTO body, @RequestParam Long userId) {
        TaskList list1 = taskListService.updateTaskListName(tasklistId, body.getName(), userId);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));

    }

    @Operation(summary = "Buscar listas por ID de usuário", description = "Endpoint alternativo para buscar listas de um usuário")
    @GetMapping("/by-user/{userId}")
    public ResponseEntity <List<TaskListResponseDTO>> searchByUserId(@PathVariable Long userId) {
        List<TaskList> listOfTaskLists = taskListService.getTaskListByUserId(userId);
        return ResponseEntity.ok(TaskListMapper.toListDTO(listOfTaskLists));

    }

    @Operation(summary = "Compartilhar lista de tarefas", description = "Compartilha uma lista com outro usuário definindo permissões")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista compartilhada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não pode compartilhar consigo mesmo", content = @Content),
            @ApiResponse(responseCode = "403", description = "Apenas o dono pode compartilhar", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista ou usuário alvo não encontrado", content = @Content)
    })
    @PostMapping("/{listId}/share")
    public ResponseEntity<Void> shareList(
            @PathVariable Long listId,
            @RequestParam Long ownerId,
            @RequestBody @Valid ShareTaskListDTO shareDTO) {
        taskListService.shareList(listId, ownerId, shareDTO.getEmail(), shareDTO.getPermission());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover compartilhamento", description = "Remove o acesso de um usuário a uma lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Compartilhamento removido com sucesso"),
            @ApiResponse(responseCode = "403", description = "Apenas o dono pode remover compartilhamento", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista, usuário ou compartilhamento não encontrado", content = @Content)
    })
    @DeleteMapping("/{listId}/share")
    public ResponseEntity<Void> unshareList(
            @PathVariable Long listId,
            @RequestParam Long ownerId,
            @RequestParam String email) {
        taskListService.unshareList(listId, ownerId, email);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar colaboradores", description = "Retorna a lista de usuários que têm acesso a esta lista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Colaboradores listados com sucesso"),
            @ApiResponse(responseCode = "403", description = "Apenas o dono pode ver colaboradores", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lista não encontrada", content = @Content)
    })
    @GetMapping("/{listId}/collaborators")
    public ResponseEntity<List<CollaboratorResponseDTO>> getCollaborators(
            @PathVariable Long listId,
            @RequestParam Long userId) {
        List<CollaboratorResponseDTO> collaborators = taskListService.getCollaborators(listId, userId);
        return ResponseEntity.ok(collaborators);
    }
}
