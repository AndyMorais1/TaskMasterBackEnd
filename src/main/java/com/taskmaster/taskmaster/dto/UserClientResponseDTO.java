package com.taskmaster.taskmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
//o que sera mostrado sobre o usuario quando for feita alguma requisicao
public class UserClientResponseDTO {
    private int id;
    private String username;
    private String email;
    private String password;

}
