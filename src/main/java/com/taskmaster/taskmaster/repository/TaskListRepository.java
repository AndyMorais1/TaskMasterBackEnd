package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository <TaskList,Long>{
    List<TaskList> findByUserId(Long userId);
}

