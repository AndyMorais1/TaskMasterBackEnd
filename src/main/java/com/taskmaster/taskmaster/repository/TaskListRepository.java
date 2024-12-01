package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskListRepository extends JpaRepository <TaskList,Long>{
    List<TaskList> findByUserId(Long userId);
    Optional<TaskList> findByNameAndUserId(String name,Long userId);
    TaskList findByName(String name);
}

