package com.taskmaster.taskmaster.dto;

import com.taskmaster.taskmaster.model.Permission;

public class CollaboratorResponseDTO {
    private Long userId;
    private String username;
    private String email;
    private Permission permission;

    public CollaboratorResponseDTO() {
    }

    public CollaboratorResponseDTO(Long userId, String username, String email, Permission permission) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.permission = permission;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
