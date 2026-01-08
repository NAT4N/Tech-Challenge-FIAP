package com.fiap.techchallenge14.security;

import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.infrastructure.security.TokenAuthFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TokenAuthFilterTest {

    private TokenAuthFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    private StringWriter responseWriter;

    private TokenMemoryPort tokenMemoryPort;

    @BeforeEach
    void setup() throws IOException {
        tokenMemoryPort = mock(TokenMemoryPort.class);
        filter = new TokenAuthFilter(tokenMemoryPort);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void doFilterInternal_ShouldAllowPublicEndpoints_Login() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v1/login");
        when(request.getMethod()).thenReturn("POST");

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void doFilterInternal_ShouldAllowPublicEndpoints_UserPost() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getMethod()).thenReturn("POST");

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void doFilterInternal_ShouldRejectInvalidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("invalid-token");

        when(tokenMemoryPort.isTokenValid("invalid-token")).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(401);
        assertEquals("{\"type\":\"/problems/unauthorized\",\"title\":\"Acesso negado\",\"status\":401,\"detail\":\"Token ausente ou inválido. Verifique o cabeçalho 'Authorization' e tente novamente.\",\"instance\":\"/v1/users\",\"properties\":null}", responseWriter.toString());
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldPassValidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("valid-token");

        when(tokenMemoryPort.isTokenValid("valid-token")).thenReturn(true);

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    void doFilterInternal_ShouldRejectWhenTokenMissing() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(401);
        assertEquals("{\"type\":\"/problems/unauthorized\",\"title\":\"Acesso negado\",\"status\":401,\"detail\":\"Token ausente ou inválido. Verifique o cabeçalho 'Authorization' e tente novamente.\",\"instance\":\"/v1/users\",\"properties\":null}", responseWriter.toString());
        verify(chain, never()).doFilter(request, response);
    }
}
