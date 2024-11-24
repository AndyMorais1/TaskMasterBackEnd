package com.taskmaster.taskmaster.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;



@Entity
public class UserClient extends User {

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TaskList> userList = new ArrayList<>();

    public UserClient() {
        super();
        this.setRole(Role.ROLE_CLIENT);
    }
}
