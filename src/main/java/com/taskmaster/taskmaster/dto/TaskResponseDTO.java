package com.taskmaster.taskmaster.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

    @Getter
    @Setter
    public class TaskResponseDTO {
        private int id;
        private String name;
        private String priority;
        private LocalDate dueDate;
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

        public TaskResponseDTO(int id, String name, String priority, LocalDate dueDate, LocalTime dueTime, String status, int listId) {
            this.id = id;
            this.name = name;
            this.priority = priority;
            this.dueDate = dueDate;
            this.dueTime = dueTime;
            this.status = status;
            this.listId = listId;
        }

        public TaskResponseDTO() {
        }
    }


