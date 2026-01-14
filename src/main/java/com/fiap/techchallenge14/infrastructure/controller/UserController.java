package com.fiap.techchallenge14.infrastructure.controller;

import com.fiap.techchallenge14.application.usecase.user.*;
import com.fiap.techchallenge14.domain.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final FindUsersUseCase findUsersUseCase;
    private final ChangeUserPasswordUseCase changeUserPasswordUseCase;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = createUserUseCase.execute(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO userRequestDTO) {

        UserResponseDTO updatedUser = updateUserUseCase.execute(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) String name) {
        List<UserResponseDTO> users = findUsersUseCase.execute(name);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeRequestDTO request
    ) {
        changeUserPasswordUseCase.execute(id, request.newPassword());
        return ResponseEntity.noContent().build();
    }
}
