package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.UserClient;
import com.taskmaster.taskmaster.repository.UserClientRepository;
import com.taskmaster.taskmaster.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor //injecao de dependencia pelo construtor
@Service
public class UserClientService {
    private final UserClientRepository userClientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserClientService(UserClientRepository userClientRepository,
                             PasswordEncoder passwordEncoder,
                             AuthenticationManager authenticationManager,
                             JwtTokenProvider jwtTokenProvider) {
        this.userClientRepository = userClientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public UserClient register(UserClient user) {
        if (userClientRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userClientRepository.save(user);

    }

    @Transactional()
    public UserClient getById(Long id) {
        return userClientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Transactional
    public UserClient getByName(String name) {
        return userClientRepository.findByUsername(name).orElseThrow(
                () -> new EntityNotFoundException("User not found")
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
            user.setPassword(passwordEncoder.encode(newPassword));
            return userClientRepository.save(user);

        }
        return null;
    }

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }
}
