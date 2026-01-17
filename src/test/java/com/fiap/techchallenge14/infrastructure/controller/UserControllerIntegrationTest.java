package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.infrastructure.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;

    @BeforeAll
    void setUp() throws Exception {

        UserCreateRequestDTO request = new UserCreateRequestDTO(
                "João da Silva",
                "joao@email.com",
                "123456",
                "Rua das Flores, 123",
                "joao.silva",
                1L
        );

        String response = mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        userId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void shouldCreateUser() throws Exception {

        UserCreateRequestDTO request = new UserCreateRequestDTO(
                "Maria Oliveira",
                "maria@email.com",
                "654321",
                "Av Paulista, 1000",
                "maria.oliveira",
                1L
        );

        mockMvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Maria Oliveira"))
                .andExpect(jsonPath("$.email").value("maria@email.com"));
    }

    @Test
    void shouldFindAllUsers() throws Exception {

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldFindUsersByName() throws Exception {

        mockMvc.perform(get("/v1/users").param("name", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("João da Silva"));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        UserUpdateRequestDTO update = new UserUpdateRequestDTO(
                "João Atualizado",
                "joao.novo@email.com",
                "Rua Nova, 500",
                "joao.atualizado",
                1L
        );

        mockMvc.perform(patch("/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Atualizado"));
    }

    @Test
    void shouldChangePassword() throws Exception {

        PasswordChangeRequestDTO request = new PasswordChangeRequestDTO("novaSenha123");

        mockMvc.perform(patch("/v1/users/" + userId + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/v1/users/" + userId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/users"))
                .andExpect(jsonPath("$.length()").value(1));
    }
}
