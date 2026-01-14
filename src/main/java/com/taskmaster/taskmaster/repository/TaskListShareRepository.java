package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.TaskList;
import com.taskmaster.taskmaster.model.TaskListShare;
import com.taskmaster.taskmaster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListShareRepository extends JpaRepository<TaskListShare, Long> {
    List<TaskListShare> findByTaskList(TaskList taskList);
    List<TaskListShare> findByUser(User user);
    Optional<TaskListShare> findByTaskListAndUser(TaskList taskList, User user);
}
