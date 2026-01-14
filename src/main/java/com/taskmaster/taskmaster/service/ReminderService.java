package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.Reminder;
import com.taskmaster.taskmaster.repository.ReminderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }


    @Transactional
    public Reminder save(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @Transactional
    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Reminder not found")
        );
    }

    @Transactional
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Transactional
    public void deleteReminderById(Long id) {
        reminderRepository.deleteById(id);
    }

}
