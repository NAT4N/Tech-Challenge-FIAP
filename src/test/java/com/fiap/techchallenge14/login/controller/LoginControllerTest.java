package com.fiap.techchallenge14.login.controller;

import com.fiap.techchallenge14.application.port.in.LoginUsecase;
import com.fiap.techchallenge14.infrastructure.controller.LoginController;
import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginUsecase loginService;

    @InjectMocks
    private LoginController loginController;

    private LoginRequestDTO requestDTO;
    private LoginResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        requestDTO = new LoginRequestDTO("guilherme", "123");
        responseDTO = new LoginResponseDTO("fake-jwt-token");
    }

    @Test
    void login_ShouldReturnOkResponseWithToken() {
        // Arrange
        when(loginService.login(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<LoginResponseDTO> response = loginController.login(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(responseDTO, response.getBody());
        assertNotNull(response.getBody());
        assertEquals("fake-jwt-token", response.getBody().token());

        verify(loginService, times(1)).login(requestDTO);
    }

    @Test
    void login_ShouldPropagateException_WhenServiceThrows() {
        // Arrange
        when(loginService.login(requestDTO))
                .thenThrow(new RuntimeException("Erro interno"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> loginController.login(requestDTO));

        assertEquals("Erro interno", ex.getMessage());
        verify(loginService, times(1)).login(requestDTO);
    }
}
