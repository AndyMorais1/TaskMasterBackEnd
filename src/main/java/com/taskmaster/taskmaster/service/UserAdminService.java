package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.UserAdmin;
import com.taskmaster.taskmaster.repository.UserAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAdminService {
    private final UserAdminRepository userAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAdminService(UserAdminRepository userAdminRepository, PasswordEncoder passwordEncoder) {
        this.userAdminRepository = userAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAdmin create(UserAdmin admin) {
        if (userAdminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return userAdminRepository.save(admin);
    }

    @Transactional
    public UserAdmin getById(Long id) {
        return userAdminRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Admin not found")
        );
    }

    @Transactional
    public List<UserAdmin> getAll() {
        return userAdminRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        userAdminRepository.deleteById(id);
    }

    @Transactional
    public UserAdmin update(Long id, UserAdmin updateData) {
        Optional<UserAdmin> adminOpt = userAdminRepository.findById(id);
        if (adminOpt.isEmpty()) {
            return null;
        }
        UserAdmin admin = adminOpt.get();
        admin.setUsername(updateData.getUsername());
        admin.setEmail(updateData.getEmail());
        if (updateData.getPassword() != null && !updateData.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(updateData.getPassword()));
        }
        return userAdminRepository.save(admin);
    }
}
