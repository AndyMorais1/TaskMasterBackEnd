package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.dto.CollaboratorResponseDTO;
import com.taskmaster.taskmaster.model.*;
import com.taskmaster.taskmaster.repository.TaskListRepository;
import com.taskmaster.taskmaster.repository.TaskListShareRepository;
import com.taskmaster.taskmaster.repository.UserClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class TaskListService {
    private final TaskListRepository taskListRepository;
    private final UserClientService userClientService;
    private final TaskListShareRepository taskListShareRepository;
    private final UserClientRepository userClientRepository;

    @Autowired
    public TaskListService(TaskListRepository taskListRepository, UserClientService userClientService,
                           TaskListShareRepository taskListShareRepository, UserClientRepository userClientRepository) {
        this.taskListRepository = taskListRepository;
        this.userClientService = userClientService;
        this.taskListShareRepository = taskListShareRepository;
        this.userClientRepository = userClientRepository;
    }

    @Transactional
    public TaskList save(TaskList list, Long userId) {
        User user = userClientService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        Optional<TaskList> existingList = taskListRepository.findByNameAndUserId(list.getName(), userId);
        if (existingList.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    " A task list with the same name already exists for this user"
            );
        }

        list.setUser(user);

        return taskListRepository.save(list);
    }



    @Transactional
    public TaskList getListById(Long id) {
        return taskListRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("List not found")
        );
    }

    // Check if user has access (owner or shared)
    public boolean hasAccess(Long listId, Long userId, Permission requiredPermission) {
        TaskList list = getListById(listId);
        if (list.getUser().getId().equals(userId)) {
            return true; // Owner has full access
        }

        User user = userClientService.getById(userId);
        Optional<TaskListShare> share = taskListShareRepository.findByTaskListAndUser(list, user);

        if (share.isPresent()) {
            if (requiredPermission == Permission.READ) {
                return true; // Any permission grants read
            }
            return share.get().getPermission() == Permission.WRITE; // Write requires WRITE permission
        }

        return false;
    }

    @Transactional
    public TaskList getListById(Long id, Long userId) {
        TaskList list = getListById(id);
        if (!hasAccess(id, userId, Permission.READ)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this list");
        }
        return list;
    }

    public TaskList getListByName(String name, Long userId) {
        Optional<TaskList> list = taskListRepository.findByNameAndUserId(name, userId);
        return list.orElseThrow(() -> new EntityNotFoundException("List not found"));
    }

    @Transactional
    public List<TaskList> getAll(){
        return taskListRepository.findAll();
    }

    @Transactional
    public void deleteList( Long id, Long userId) {
        TaskList list = getListById(id);
        if (!list.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can delete this list");
        }
            taskListRepository.delete(list);
    }


    @Transactional
    public List<Task> getTasks(Long listId, Long userId) {
        if (!hasAccess(listId, userId, Permission.READ)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this list");
        }
        TaskList list = getListById(listId);
        return list.getTasks();
    }

    @Transactional
    public TaskList updateTaskListName(Long id, String newName, Long userId) {
        TaskList list = getListById(id);
        if (!hasAccess(id, userId, Permission.WRITE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this list");
        }
      list.setName(newName);
      return taskListRepository.save(list);
    }

    @Transactional
    public List<TaskList> getTaskListByUserId (Long userId) {
        // Get lists owned by user
        List<TaskList> ownedLists = taskListRepository.getTaskListByUserId(userId);
        
        // Get lists shared with user
        User user = userClientService.getById(userId);
        List<TaskListShare> shares = taskListShareRepository.findByUser(user);
        List<TaskList> sharedLists = shares.stream()
                .map(TaskListShare::getTaskList)
                .collect(Collectors.toList());

        // Combine both
        return Stream.concat(ownedLists.stream(), sharedLists.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void shareList(Long listId, Long ownerId, String emailToShareWith, Permission permission) {
        TaskList list = getListById(listId);

        // Verify owner
        if (!list.getUser().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can share this list");
        }

        // Find user to share with
        User userToShare = userClientRepository.findByEmail(emailToShareWith)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + emailToShareWith + " not found"));

        if (userToShare.getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot share list with yourself");
        }

        // Check if already shared
        Optional<TaskListShare> existingShare = taskListShareRepository.findByTaskListAndUser(list, userToShare);
        if (existingShare.isPresent()) {
            TaskListShare share = existingShare.get();
            share.setPermission(permission); // Update permission
            taskListShareRepository.save(share);
        } else {
            TaskListShare newShare = new TaskListShare();
            newShare.setTaskList(list);
            newShare.setUser(userToShare);
            newShare.setPermission(permission);
            taskListShareRepository.save(newShare);
        }
    }

    @Transactional
    public void unshareList(Long listId, Long ownerId, String emailToUnshare) {
        TaskList list = getListById(listId);

        // Verify owner
        if (!list.getUser().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can unshare this list");
        }

        // Find user to unshare
        User userToUnshare = userClientRepository.findByEmail(emailToUnshare)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + emailToUnshare + " not found"));

        // Find share entry
        TaskListShare share = taskListShareRepository.findByTaskListAndUser(list, userToUnshare)
                .orElseThrow(() -> new EntityNotFoundException("This list is not shared with " + emailToUnshare));

        taskListShareRepository.delete(share);
    }

    public List<CollaboratorResponseDTO> getCollaborators(Long listId, Long userId) {
        TaskList list = getListById(listId);

        // Verify owner
        if (!list.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can view collaborators");
        }

        List<TaskListShare> shares = taskListShareRepository.findByTaskList(list);

        return shares.stream()
                .map(share -> new CollaboratorResponseDTO(
                        share.getUser().getId(),
                        share.getUser().getUsername(),
                        share.getUser().getEmail(),
                        share.getPermission()
                ))
                .collect(Collectors.toList());
    }
}
