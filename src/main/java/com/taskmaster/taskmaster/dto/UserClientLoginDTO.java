package com.taskmaster.taskmaster.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserClientLoginDTO {
    private String username;
    private String password;
}
