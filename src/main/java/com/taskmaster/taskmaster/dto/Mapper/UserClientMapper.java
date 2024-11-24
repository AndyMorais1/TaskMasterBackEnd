package com.taskmaster.taskmaster.dto.Mapper;

import com.taskmaster.taskmaster.dto.UserClientCreateDTO;
import com.taskmaster.taskmaster.dto.UserClientResponseDTO;
import com.taskmaster.taskmaster.model.UserClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserClientMapper {

    //mapear DTO para UserClient(Model) (criar/entrada)
    public static UserClient toUser(UserClientCreateDTO userClientCreateDTO) {
        return (UserClient) new ModelMapper().map(userClientCreateDTO, UserClient.class);
    }

    //mapear de UserClient(Model) para DTO (resposta/saida/mostrar)
   public static UserClientResponseDTO toDTO(UserClient user) {
        String role = user.getRole().name().substring("ROLE_".length());
       PropertyMap<UserClient, UserClientResponseDTO> props = new PropertyMap<UserClient, UserClientResponseDTO>() {
           @Override
           protected void configure() {
               map().setRole(role);
           }
       };
       ModelMapper mapper = new ModelMapper();
       mapper.addMappings(props);
       return mapper.map(user, UserClientResponseDTO.class);
   }

   public static List<UserClientResponseDTO> toListDTO(List<UserClient> users) {
        return users.stream().map(user -> toDTO(user)).collect(Collectors.toList());
   }

}
