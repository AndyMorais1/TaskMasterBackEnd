package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.Reminder;
import com.taskmaster.taskmaster.service.ReminderService;
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
@Tag(name = "Lembretes", description = "Tudo relacionado a Lembretes de tarefas")
@RestController
@RequestMapping("api/v1/reminders")
public class ReminderController {
    @Autowired
    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }
    @Operation(summary = "Criar um novo lembrete", description = "Cria um novo lembrete para uma tarefa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lembrete criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reminder.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody Reminder reminder) {
     Reminder savedReminder = reminderService.save(reminder);
     return ResponseEntity.ok(savedReminder);
    }

    @Operation(summary = "Buscar lembrete por ID", description = "Retorna os detalhes de um lembrete específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lembrete encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reminder.class))),
            @ApiResponse(responseCode = "404", description = "Lembrete não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getById(@PathVariable Long id) {
        Reminder remind1= reminderService.getReminderById(id);
        return ResponseEntity.status(200).body(remind1);
    }

    @Operation(summary = "Listar todos os lembretes", description = "Retorna todos os lembretes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de lembretes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Reminder>> getAll() {
        List<Reminder> reminders = reminderService.getAllReminders();
        return ResponseEntity.ok(reminders);
    }

    @Operation(summary = "Deletar lembrete", description = "Remove um lembrete existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lembrete deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Lembrete não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Reminder> delete(@PathVariable Long id) {
        Reminder reminder = reminderService.getReminderById(id);
        reminderService.deleteReminderById(id);
        return ResponseEntity.status(200).body(reminder);
    }

}
