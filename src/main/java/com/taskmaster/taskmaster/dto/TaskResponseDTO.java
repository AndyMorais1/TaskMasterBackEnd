package com.taskmaster.taskmaster.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taskmaster.taskmaster.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class TaskResponseDTO {
        private int id;
        private String name;
        private String priority;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDate dueDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private LocalTime dueTime;
        private String status;
        private int listId;
    }


