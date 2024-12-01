package com.taskmaster.taskmaster.dto;

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
        private LocalDate dueDate;
        private LocalTime dueTime;
        private String status;
        private int listId;
    }


