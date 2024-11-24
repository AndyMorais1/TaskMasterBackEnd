package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.UserClient;
import com.taskmaster.taskmaster.repository.UserClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor //injecao de dependencia pelo construtor
@Service
public class UserClientService {
    private final UserClientRepository userClientRepository;

    @Autowired
    public UserClientService(UserClientRepository userClientRepository) {
        this.userClientRepository = userClientRepository;
    }

    @Transactional
    public UserClient save(UserClient user) {
        return userClientRepository.save(user);

    }

    @Transactional()
    public UserClient getById(Long id) {
        return userClientRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Transactional
    public List<UserClient> getAll() {
        return userClientRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        userClientRepository.deleteById(id);

    }

    @Transactional
    public UserClient updateName(Long id, String newName) {
        Optional<UserClient> userClient = userClientRepository.findById(id);
        if (userClient.isPresent()) {
            UserClient user = userClient.get();
            user.setUsername(newName);
            return userClientRepository.save(user);
        }
        return null;
    }

    @Transactional
    public UserClient updatePassword(Long id, String newPassword) {
        Optional<UserClient> userClient = userClientRepository.findById(id);
        if (userClient.isPresent()) {
            UserClient user = userClient.get();
            user.setPassword(newPassword);
            return userClientRepository.save(user);

        }
        return null;
    }


}
