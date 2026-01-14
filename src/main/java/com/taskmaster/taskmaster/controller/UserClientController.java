package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.UserClientMapper;
import com.taskmaster.taskmaster.dto.UserClientCreateDTO;
import com.taskmaster.taskmaster.dto.UserClientLoginDTO;
import com.taskmaster.taskmaster.dto.UserClientResponseDTO;
import com.taskmaster.taskmaster.dto.UserUpdatePasswordDTO;
import com.taskmaster.taskmaster.dto.UserUpdateUsernameDTO;
import com.taskmaster.taskmaster.model.UserClient;
import com.taskmaster.taskmaster.service.UserClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "Usuarios", description = "Tudo relacionado a usuarios")
//a injecao de dependencia sera feita via metodo construtor
//o spring entende que e uma bean controller para uso de requisicoes Rest (baseada em HTTP)
@RestController

@RequestMapping("api/v1/users")
public class UserClientController {

    private final UserClientService userClientService;//injecao de dependencia para o service

    @Autowired
    public UserClientController(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @Operation(summary = "Criar um novo usuario")
    @PostMapping
    public ResponseEntity<UserClientResponseDTO> create(@Valid @RequestBody UserClientCreateDTO createDTO) {
        UserClient user1 = userClientService.register(UserClientMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(UserClientMapper.toDTO(user1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserClientResponseDTO> searchById(@PathVariable Long id) {
        UserClient user = userClientService.getById(id);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserClientResponseDTO> searchByUsername(@PathVariable String username) {
        UserClient user = userClientService.getByName(username);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserClientResponseDTO>> listUsers() {
        List<UserClient> userList = userClientService.getAll();
        return ResponseEntity.ok(UserClientMapper.toListDTO(userList));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UserClientResponseDTO> delete(@PathVariable Long id) {
        UserClient user1 = userClientService.getById(id);
        UserClientResponseDTO dtoUser1 = UserClientMapper.toDTO(user1);
        userClientService.delete(id);
        return ResponseEntity.ok().body(dtoUser1);
    }

    @PutMapping("/{userid}/username")
    public ResponseEntity<UserClientResponseDTO> updateName(@PathVariable Long userid, @Valid @RequestBody UserUpdateUsernameDTO body) {
        UserClient updatedUser = userClientService.updateName(userid, body.getUsername());
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UserClientMapper.toDTO(updatedUser));
    }

    @PutMapping("/{userid}/password")
    public ResponseEntity<UserClientResponseDTO> updatePassword(@PathVariable Long userid, @Valid @RequestBody UserUpdatePasswordDTO body) {
        UserClient updatedUser = userClientService.updatePassword(userid, body.getPassword());
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UserClientMapper.toDTO(updatedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserClientLoginDTO dto) {
        String token = userClientService.login(dto.getUsername(), dto.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
