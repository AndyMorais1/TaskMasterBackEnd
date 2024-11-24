package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository <Reminder, Long>{
}
