package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.port.in.UserUsecase;
import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.domain.model.Role;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserUsecase userUsecase;

    @MockitoBean
    private TokenMemoryPort tokenMemoryPort;

    @MockitoBean
    private RoleRepositoryPort roleRepositoryPort;

    @MockitoBean
    private UserRepositoryPort userRepositoryPort;

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
                "Role"
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

        when(userUsecase.save(any())).thenReturn(userResponse);
        when(roleRepositoryPort.findById(1L)).thenReturn(Optional.of(new Role(1L, "Role", "Description")));
        when(userRepositoryPort.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(userRepositoryPort.findByLogin("login")).thenReturn(Optional.empty());

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

        when(userUsecase.update(eq(1L), any())).thenReturn(userResponse);
        when(roleRepositoryPort.findById(1L)).thenReturn(Optional.of(new Role(1L, "Role", "Description")));

        mockMvc.perform(patch("/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_ShouldReturn204() throws Exception {
        doNothing().when(userUsecase).delete(1L);

        mockMvc.perform(delete("/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsers_ShouldReturnList() throws Exception {
        when(userUsecase.findUsers(any())).thenReturn(List.of(userResponse));

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void changePassword_ShouldReturn204() throws Exception {
        PasswordChangeRequestDTO request = new PasswordChangeRequestDTO("newPassword123");

        doNothing().when(userUsecase).changePassword(eq(1L), any());

        mockMvc.perform(patch("/v1/users/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}
