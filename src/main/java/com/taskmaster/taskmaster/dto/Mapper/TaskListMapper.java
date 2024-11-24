package com.taskmaster.taskmaster.dto.Mapper;

import com.taskmaster.taskmaster.dto.TaskListCreateDTO;
import com.taskmaster.taskmaster.dto.TaskListResponseDTO;
import com.taskmaster.taskmaster.model.TaskList;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TaskListMapper {

    public static TaskList toTaskList(TaskListCreateDTO taskListCreateDTO) {
        return (TaskList) new ModelMapper().map(taskListCreateDTO, TaskList.class);
    }

    public static TaskListResponseDTO toDTO(TaskList taskList) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(taskList, TaskListResponseDTO.class) ;
    }

    public static List<TaskListResponseDTO> toListDTO(List<TaskList> taskLists) {
        return taskLists.stream().map(taskList -> toDTO(taskList)).collect(Collectors.toList());
    }
}
