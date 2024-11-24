package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.UserClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserClientRepository extends JpaRepository <UserClient, Long> {
}