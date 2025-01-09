package com.taskmaster.taskmaster.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter @Setter
@Entity @AllArgsConstructor @NoArgsConstructor
@Table(name = "reminders")
public class Reminder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "remin_date", nullable = false)
    private LocalDate reminderDate;

    @Column(name = "remin_time", nullable = false)
    private LocalTime reminderTime;

    @ManyToOne
    @JsonIgnore
    @JsonBackReference
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    //sistema de auditoria

    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();

    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "create_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", reminderDate=" + reminderDate +
                ", reminderTime=" + reminderTime +
                ", task=" + task +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return Objects.equals(id, reminder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}