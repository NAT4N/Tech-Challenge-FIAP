package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.usecase.user.*;
import com.fiap.techchallenge14.infrastructure.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import com.fiap.techchallenge14.infrastructure.security.InMemoryToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    @MockitoBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockitoBean
    private FindUsersUseCase findUsersUseCase;

    @MockitoBean
    private ChangeUserPasswordUseCase changeUserPasswordUseCase;

    @MockitoBean
    private InMemoryToken inMemoryToken;

    @MockitoBean
    private RoleRepository roleRepository;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponseDTO(
                1L,
                "User Name",
                "user@email.com",
                "Address",
                "login",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                1L
        );
    }

    @Test
    void createUser_ShouldReturn201() throws Exception {
        UserCreateRequestDTO request = new UserCreateRequestDTO(
                "User Name",
                "user@email.com",
                "password123",
                "Address",
                "login",
                1L
        );

        when(createUserUseCase.execute(any())).thenReturn(userResponse);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new RoleEntity(1L, "Role")));

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateUser_ShouldReturn200() throws Exception {
        UserUpdateRequestDTO request = new UserUpdateRequestDTO(
                "Updated Name",
                "updated@email.com",
                "Updated Address",
                "updatedLogin",
                1L
        );

        when(updateUserUseCase.execute(eq(1L), any(UserUpdateRequestDTO.class))).thenReturn(userResponse);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(new RoleEntity(1L, "Role")));

        mockMvc.perform(patch("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteUser_ShouldReturn204() throws Exception {
        doNothing().when(deleteUserUseCase).execute(1L);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsers_ShouldReturnList() throws Exception {
        when(findUsersUseCase.execute(any())).thenReturn(List.of(userResponse));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void changePassword_ShouldReturn204() throws Exception {
        PasswordChangeRequestDTO request = new PasswordChangeRequestDTO("newPassword123");

        doNothing().when(changeUserPasswordUseCase).execute(eq(1L), anyString());

        mockMvc.perform(patch("/v1/users/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
