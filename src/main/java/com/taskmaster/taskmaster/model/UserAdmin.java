package com.taskmaster.taskmaster.model;

import jakarta.persistence.*;


@Entity
public class UserAdmin  extends User {
    public UserAdmin() {
        super();
        this.setRole(Role.ROLE_CLIENT);
    }
}
