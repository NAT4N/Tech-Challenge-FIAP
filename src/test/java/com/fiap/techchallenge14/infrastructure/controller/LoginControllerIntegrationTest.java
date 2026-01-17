package com.fiap.techchallenge14.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.usecase.login.AuthenticateUserUseCase;
import com.fiap.techchallenge14.infrastructure.dto.LoginRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.LoginResponseDTO;
import com.fiap.techchallenge14.infrastructure.security.InMemoryToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticateUserUseCase authenticateUserUseCase;

    @MockitoBean
    private InMemoryToken inMemoryToken;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ShouldReturn200() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("user", "pass");
        LoginResponseDTO response = new LoginResponseDTO("token");

        when(authenticateUserUseCase.execute(any())).thenReturn(response);

        mockMvc.perform(post("/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }
}
