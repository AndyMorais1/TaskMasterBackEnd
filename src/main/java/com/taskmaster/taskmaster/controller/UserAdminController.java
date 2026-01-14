package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.dto.Mapper.UserAdminMapper;
import com.taskmaster.taskmaster.dto.UserAdminCreateDTO;
import com.taskmaster.taskmaster.dto.UserAdminResponseDTO;
import com.taskmaster.taskmaster.model.UserAdmin;
import com.taskmaster.taskmaster.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Criar um novo admin")
    @PostMapping
    public ResponseEntity<UserAdminResponseDTO> create(@Valid @RequestBody UserAdminCreateDTO createDTO) {
        UserAdmin admin = userAdminService.create(UserAdminMapper.toUser(createDTO));
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(UserAdminMapper.toDTO(admin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> getById(@PathVariable Long id) {
        UserAdmin admin = userAdminService.getById(id);
        return ResponseEntity.ok(UserAdminMapper.toDTO(admin));
    }

    @GetMapping
    public ResponseEntity<List<UserAdminResponseDTO>> listAdmins() {
        List<UserAdmin> admins = userAdminService.getAll();
        return ResponseEntity.ok(UserAdminMapper.toListDTO(admins));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserAdminResponseDTO> delete(@PathVariable Long id) {
        UserAdmin admin = userAdminService.getById(id);
        UserAdminResponseDTO dto = UserAdminMapper.toDTO(admin);
        userAdminService.delete(id);
        return ResponseEntity.ok(dto);
    }

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

