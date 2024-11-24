package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.Task;
import com.taskmaster.taskmaster.model.TaskList;
import com.taskmaster.taskmaster.model.User;
import com.taskmaster.taskmaster.repository.TaskListRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TaskListService {
    private final TaskListRepository taskListRepository;
    private final UserClientService userClientService;

    @Autowired
    public TaskListService(TaskListRepository taskListRepository, UserClientService userClientService) {
        this.taskListRepository = taskListRepository;
        this.userClientService = userClientService;
    }

    @Transactional
    public TaskList save(TaskList list, Long userId) {
        User user = userClientService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        list.setUser(user);

        return taskListRepository.save(list);
    }



    @Transactional
    public TaskList getListById(Long id) {
        return taskListRepository.findById(id).orElseThrow(
                () -> new RuntimeException("List not found")
        );
    }

    @Transactional
    public List<TaskList> getAll(){
        return taskListRepository.findAll();
    }

    @Transactional
    public void deleteList( Long id) {
        TaskList list = getListById(id);
            taskListRepository.delete(list);
    }


    @Transactional
    public List<Task> getTasks(Long listId) {
        TaskList list = getListById(listId);
        return list.getTasks();
    }

    @Transactional
    public TaskList updateTaskListName(Long id, String newName) {
        TaskList list = getListById(id);
      list.setName(newName);
      return taskListRepository.save(list);
    }
}
