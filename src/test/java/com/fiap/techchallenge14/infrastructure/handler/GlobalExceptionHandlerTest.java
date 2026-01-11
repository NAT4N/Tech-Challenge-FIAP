package com.fiap.techchallenge14.infrastructure.handler;

import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.exception.handler.GlobalExceptionHandler;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleRuntime_ShouldReturnNotFound() {
        RuntimeException ex = new RuntimeException("Erro de runtime");

        ProblemDetail response = handler.handleRuntime(ex);

        assertEquals(404, response.getStatus());
        assertEquals("Erro de runtime", response.getDetail());
    }

    @Test
    void handleUserException_ShouldReturnBadRequest() {
        UserException ex = new UserException("Erro de usuário");

        ProblemDetail response = handler.handleUserException(ex);

        assertEquals(400, response.getStatus());
        assertEquals("Erro de usuário", response.getDetail());
    }

    @Test
    void handleRestaurantException_ShouldReturnBadRequest() {
        RestaurantException ex = new RestaurantException("Erro de restaurante");

        ProblemDetail response = handler.handleRestaurantException(ex);

        assertEquals(400, response.getStatus());
        assertEquals("Erro de restaurante", response.getDetail());
    }

    @Test
    void handleLoginException_ShouldReturnNotFound() {
        LoginException ex = new LoginException("Login erro");

        ProblemDetail response = handler.handleRuntime(ex);

        assertEquals(404, response.getStatus());
        assertEquals("Login erro", response.getDetail());
    }

    @Test
    void handleValidation_ShouldReturnFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("user", "name", "Nome obrigatório");
        FieldError error2 = new FieldError("user", "email", "Email inválido");

        when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ProblemDetail response = handler.handleValidation(ex);

        assertEquals(400, response.getStatus());
        assertNotNull(response.getDetail());
        assertNotNull(response.getProperties());
        Object errorsObj = response.getProperties().get("errors");
        assertNotNull(errorsObj);

        Map<?, ?> errors = (Map<?, ?>) errorsObj;
        assertEquals(2, errors.size());
        assertTrue(errors.containsKey("name"));
        assertTrue(errors.containsKey("email"));
    }
}
