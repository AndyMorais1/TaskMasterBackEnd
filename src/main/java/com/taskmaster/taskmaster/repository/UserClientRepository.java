package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserClientRepository extends JpaRepository <UserClient, Long> {
    Optional<UserClient> findByUsernameAndPassword(String username, String password);
    Optional<UserClient> findByUsername(String username);
}