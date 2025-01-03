package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.UserClientMapper;
import com.taskmaster.taskmaster.dto.UserClientCreateDTO;
import com.taskmaster.taskmaster.dto.UserClientLoginDTO;
import com.taskmaster.taskmaster.dto.UserClientResponseDTO;
import com.taskmaster.taskmaster.model.UserClient;
import com.taskmaster.taskmaster.service.UserClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

//path de acesso ao recurso (controller), ou seja,
//o caminho base dentro dos recursos da controller
@RequestMapping("api/v1/userClients")//caminho
public class UserClientController {

    @Autowired
    private final UserClientService userClientService;//injecao de dependencia para o service

    @Autowired
    public UserClientController(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @Operation(summary = "Criar um novo usuario")
    //cria usuario no banco
    @PostMapping
    public ResponseEntity<UserClientResponseDTO> create(@Valid @RequestBody UserClientCreateDTO createDTO) {
        UserClient user1 = userClientService.save(UserClientMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(UserClientMapper.toDTO(user1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserClientResponseDTO> searchById(@PathVariable Long id) {
        UserClient user = userClientService.getById(id);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @GetMapping("/getbyname/{username}")
    public ResponseEntity<UserClientResponseDTO> searchByUsername(@PathVariable String username) {
        UserClient user = userClientService.getByName(username);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @GetMapping("/userlist")
    public ResponseEntity<List<UserClientResponseDTO>> searchUserList() {
        List<UserClient> userList = userClientService.getAll();
        return ResponseEntity.ok(UserClientMapper.toListDTO(userList));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserClientResponseDTO> delete(@PathVariable Long id) {
        UserClient user1 = userClientService.getById(id);
        UserClientResponseDTO dtoUser1 = UserClientMapper.toDTO(user1);
        userClientService.delete(id);
        return ResponseEntity.ok().body(dtoUser1);
    }

    // por alterar com request body
    @PostMapping("/updatename/{userid}/{newname}")
    public ResponseEntity<UserClientResponseDTO> updateName(@PathVariable Long userid, @PathVariable String newname) {
        UserClient user1 = userClientService.getById(userid);
        user1.setUsername(newname);
        userClientService.save(user1);
        UserClientResponseDTO dtoUser1 = UserClientMapper.toDTO(userClientService.save(user1));
        return ResponseEntity.ok().body(dtoUser1);
    }

    // por alterar com request body
    @PostMapping("/updatepassword/{userid}/{newpassword}")
    public ResponseEntity<UserClientResponseDTO> updatePassword(@PathVariable Long userid, @PathVariable String newpassword) {
        UserClient user1 = userClientService.getById(userid);
        user1.setPassword(newpassword);
        userClientService.save(user1);
        UserClientResponseDTO dtoUser1 = UserClientMapper.toDTO(userClientService.save(user1));
        return ResponseEntity.ok().body(dtoUser1);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody UserClientLoginDTO dto) {
        boolean isValid = userClientService.verifyLogin(dto.getUsername(), dto.getPassword());
        Map<String, String> response = new HashMap<>();
        if (isValid) {
            response.put("message", "Login successful");
        } else {
            response.put("message", "Invalid username or password");
        }
        return response;
    }
}
