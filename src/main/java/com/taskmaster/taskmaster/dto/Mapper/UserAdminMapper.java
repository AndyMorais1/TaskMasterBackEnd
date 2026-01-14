package com.taskmaster.taskmaster.dto.Mapper;

import com.taskmaster.taskmaster.dto.UserAdminCreateDTO;
import com.taskmaster.taskmaster.dto.UserAdminResponseDTO;
import com.taskmaster.taskmaster.model.UserAdmin;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserAdminMapper {
    public static UserAdmin toUser(UserAdminCreateDTO userAdminCreateDTO) {
        return new ModelMapper().map(userAdminCreateDTO, UserAdmin.class);
    }

    public static UserAdminResponseDTO toDTO(UserAdmin user) {
        return new ModelMapper().map(user, UserAdminResponseDTO.class);
    }

    public static List<UserAdminResponseDTO> toListDTO(List<UserAdmin> users) {
        return users.stream().map(UserAdminMapper::toDTO).collect(Collectors.toList());
    }
}

