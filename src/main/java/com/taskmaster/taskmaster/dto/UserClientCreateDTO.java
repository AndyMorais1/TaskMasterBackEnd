package com.taskmaster.taskmaster.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
//DTO que determinara o que sera pedido quando estiver a criar usuario
public class UserClientCreateDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail fornecido não é válido.")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
