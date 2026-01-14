package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.TaskListMapper;
import com.taskmaster.taskmaster.dto.Mapper.TaskMapper;
import com.taskmaster.taskmaster.dto.TaskListCreateDTO;
import com.taskmaster.taskmaster.dto.TaskListResponseDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.model.TaskList;
import com.taskmaster.taskmaster.service.TaskListService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Criar uma nova lista de tarefas para um usuario")
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


    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponseDTO> searchById(@PathVariable Long id) {
        TaskList list1 = taskListService.getListById(id);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
    }

    @GetMapping("/by-name/{listName}")
    public ResponseEntity<TaskListResponseDTO> searchByListName(@PathVariable String listName) {
        TaskList list1 = taskListService.getListByName(listName);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
    }

    @GetMapping
   public ResponseEntity<List<TaskListResponseDTO>> listLists() {
     List<TaskList> TaskListsList = taskListService.getAll();
     return ResponseEntity.ok(TaskListMapper.toListDTO(TaskListsList));
    }

   @DeleteMapping("/{listId}")
    public ResponseEntity<TaskListResponseDTO> delete(@PathVariable Long listId) {
        TaskList list1 = taskListService.getListById(listId);
       taskListService.deleteList(listId);
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));
   }

   @GetMapping("/{listId}/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@PathVariable Long listId)
   {
       List<Task> tasks = taskListService.getTasks( listId);
       List<TaskResponseDTO> dtoTasks = TaskMapper.toListDTO(tasks);
       return ResponseEntity.status(200).body(dtoTasks);
   }


   @PutMapping("/{tasklistId}/name")
    public ResponseEntity<TaskListResponseDTO> updateTaskListName(@PathVariable Long tasklistId, @RequestBody TaskListCreateDTO body) {
        TaskList list1 = taskListService.updateTaskListName(tasklistId, body.getName());
        return ResponseEntity.status(200).body(TaskListMapper.toDTO(list1));

   }

   @GetMapping("/by-user/{userId}")
    public ResponseEntity <List<TaskListResponseDTO>> searchByUserId(@PathVariable Long userId) {
        List<TaskList> listOfTaskLists = taskListService.getTaskListByUserId(userId);
        return ResponseEntity.ok(TaskListMapper.toListDTO(listOfTaskLists));

   }


}
