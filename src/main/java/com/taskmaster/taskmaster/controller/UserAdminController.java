package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.UserAdminMapper;
import com.taskmaster.taskmaster.dto.UserAdminCreateDTO;
import com.taskmaster.taskmaster.dto.UserAdminResponseDTO;
import com.taskmaster.taskmaster.model.UserAdmin;
import com.taskmaster.taskmaster.service.UserAdminService;
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

import java.util.List;

@Tag(name = "Admins", description = "Tudo relacionado a usuarios administradores")
@RestController
@RequestMapping("api/v1/admins")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @Autowired
    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @Operation(summary = "Criar um novo admin", description = "Cria um novo usuário com privilégios de administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Admin criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdminResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Admin já existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UserAdminResponseDTO> create(@Valid @RequestBody UserAdminCreateDTO createDTO) {
        UserAdmin admin = userAdminService.create(UserAdminMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(UserAdminMapper.toDTO(admin));
    }

    @Operation(summary = "Buscar admin por ID", description = "Retorna os detalhes de um administrador específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserAdminResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Admin não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> getById(@PathVariable Long id) {
        UserAdmin admin = userAdminService.getById(id);
        return ResponseEntity.ok(UserAdminMapper.toDTO(admin));
    }

    @Operation(summary = "Listar todos os admins", description = "Retorna uma lista de todos os administradores")
    @ApiResponse(responseCode = "200", description = "Lista de admins retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UserAdminResponseDTO>> listAdmins() {
        List<UserAdmin> admins = userAdminService.getAll();
        return ResponseEntity.ok(UserAdminMapper.toListDTO(admins));
    }

    @Operation(summary = "Deletar admin", description = "Remove um administrador do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Admin não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> delete(@PathVariable Long id) {
        UserAdmin admin = userAdminService.getById(id);
        UserAdminResponseDTO dto = UserAdminMapper.toDTO(admin);
        userAdminService.delete(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Atualizar admin", description = "Atualiza os dados de um administrador existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Admin não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserAdminCreateDTO body) {
        UserAdmin updateData = UserAdminMapper.toUser(body);
        UserAdmin updated = userAdminService.update(id, updateData);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserAdminMapper.toDTO(updated));
    }
}

