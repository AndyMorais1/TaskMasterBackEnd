package com.taskmaster.taskmaster.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminCreateDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail fornecido não é válido.")
    private String email;
}

