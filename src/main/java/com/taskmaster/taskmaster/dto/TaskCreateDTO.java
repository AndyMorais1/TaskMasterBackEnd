package com.taskmaster.taskmaster.dto;

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
public class TaskCreateDTO {
    private String name;
    private String priority;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Long listId;
}
