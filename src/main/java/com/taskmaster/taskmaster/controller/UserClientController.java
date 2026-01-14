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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Criar um novo usuario", description = "Registra um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserClientResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Usuário ou email já existente", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserClientResponseDTO> create(@Valid @RequestBody UserClientCreateDTO createDTO) {
        UserClient user1 = userClientService.register(UserClientMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(UserClientMapper.toDTO(user1));
    }

    @Operation(summary = "Buscar usuario por ID", description = "Retorna os detalhes de um usuário específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserClientResponseDTO> searchById(@PathVariable Long id) {
        UserClient user = userClientService.getById(id);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @Operation(summary = "Buscar usuario por nome de usuário", description = "Retorna os detalhes de um usuário específico pelo username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserClientResponseDTO> searchByUsername(@PathVariable String username) {
        UserClient user = userClientService.getByName(username);
        return ResponseEntity.ok(UserClientMapper.toDTO(user));
    }

    @Operation(summary = "Listar todos os usuarios", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UserClientResponseDTO>> listUsers() {
        List<UserClient> userList = userClientService.getAll();
        return ResponseEntity.ok(UserClientMapper.toListDTO(userList));
    }


    @Operation(summary = "Deletar usuario", description = "Remove um usuário do sistema pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserClientResponseDTO> delete(@PathVariable Long id) {
        UserClient user1 = userClientService.getById(id);
        UserClientResponseDTO dtoUser1 = UserClientMapper.toDTO(user1);
        userClientService.delete(id);
        return ResponseEntity.ok().body(dtoUser1);
    }

    @Operation(summary = "Atualizar nome de usuário", description = "Atualiza o username de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/{userid}/username")
    public ResponseEntity<UserClientResponseDTO> updateName(@PathVariable Long userid, @Valid @RequestBody UserUpdateUsernameDTO body) {
        UserClient updatedUser = userClientService.updateName(userid, body.getUsername());
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UserClientMapper.toDTO(updatedUser));
    }

    @Operation(summary = "Atualizar senha", description = "Atualiza a senha de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/{userid}/password")
    public ResponseEntity<UserClientResponseDTO> updatePassword(@PathVariable Long userid, @Valid @RequestBody UserUpdatePasswordDTO body) {
        UserClient updatedUser = userClientService.updatePassword(userid, body.getPassword());
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(UserClientMapper.toDTO(updatedUser));
    }

    @Operation(summary = "Login de usuário", description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"token\": \"jwt_token_here\"}"))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserClientLoginDTO dto) {
        String token = userClientService.login(dto.getUsername(), dto.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
}
