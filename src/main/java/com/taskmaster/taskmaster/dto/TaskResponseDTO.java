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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        public LocalTime getDueTime() {
            return dueTime;
        }

        public void setDueTime(LocalTime dueTime) {
            this.dueTime = dueTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getListId() {
            return listId;
        }

        public void setListId(int listId) {
            this.listId = listId;
        }
    }


