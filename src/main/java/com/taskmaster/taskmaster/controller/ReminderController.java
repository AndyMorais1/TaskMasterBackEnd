package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.Reminder;
import com.taskmaster.taskmaster.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Criar um novo lembrete")
    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody Reminder reminder) {
     Reminder savedReminder = reminderService.save(reminder);
     return ResponseEntity.ok(savedReminder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getById(@PathVariable Long id) {
        Reminder remind1= reminderService.getReminderById(id);
        return ResponseEntity.status(200).body(remind1);
    }

    @GetMapping
    public ResponseEntity<List<Reminder>> getAll() {
        List<Reminder> reminders = reminderService.getAllReminders();
        return ResponseEntity.ok(reminders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reminder> delete(@PathVariable Long id) {
        Reminder reminder = reminderService.getReminderById(id);
        reminderService.deleteReminderById(id);
        return ResponseEntity.status(200).body(reminder);
    }

}
