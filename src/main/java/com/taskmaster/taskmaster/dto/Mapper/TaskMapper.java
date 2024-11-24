package com.taskmaster.taskmaster.dto.Mapper;

import com.taskmaster.taskmaster.dto.TaskCreateDTO;
import com.taskmaster.taskmaster.dto.TaskResponseDTO;
import com.taskmaster.taskmaster.model.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static Task toTask(TaskCreateDTO taskCreateDTO) {
        return (Task) new ModelMapper().map(taskCreateDTO, Task.class);
    }

    public static TaskResponseDTO toDTO(Task task) {
        // Verifique se os campos Priority e Status são nulos antes de acessar .name()
        String priority = (task.getPriority() != null) ?
                task.getPriority().name().substring("PRIORITY_".length()) : null;

        String status = (task.getStatus() != null) ?
                task.getStatus().name().substring("STATUS_".length()) : null;

        // Use o ModelMapper para mapear propriedades
        ModelMapper mapper = new ModelMapper();
        PropertyMap<Task, TaskResponseDTO> props = new PropertyMap<Task, TaskResponseDTO>() {
            @Override
            protected void configure() {
                // Configure os valores derivados
                map().setPriority(priority);
                map().setStatus(status);
            }
        };

        mapper.addMappings(props);
        return mapper.map(task, TaskResponseDTO.class);
    }


    public static List<TaskResponseDTO> toListDTO(List<Task> tasks){
        return tasks.stream().map(task -> toDTO(task)).collect(Collectors.toList());
    }

}
