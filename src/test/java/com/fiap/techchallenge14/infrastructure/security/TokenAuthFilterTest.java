package com.fiap.techchallenge14.infrastructure.security;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenAuthFilterTest {

    @Mock
    private InMemoryToken inMemoryToken;

    @Mock
    private FilterChain filterChain;

    private TokenAuthFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TokenAuthFilter(inMemoryToken);
    }

    @Test
    void doFilterInternal_ShouldAllowPublicEndpoint_Login() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(inMemoryToken);
        assertEquals(200, response.getStatus()); // MockHttpServletResponse default
    }

    @Test
    void doFilterInternal_ShouldAllowPublicEndpoint_CreateUser_PostUsers() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/users");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(inMemoryToken);
        assertEquals(200, response.getStatus());
    }

    @Test
    void doFilterInternal_ShouldReturn401_WhenTokenMissing_OnProtectedEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/roles");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(any(), any());
        verifyNoInteractions(inMemoryToken);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/problem+json"));
        assertTrue(response.getContentAsString().contains("Acesso negado"));
        assertTrue(response.getContentAsString().contains("Token ausente ou inválido"));
        assertTrue(response.getContentAsString().contains("/problems/unauthorized"));
    }

    @Test
    void doFilterInternal_ShouldReturn401_WhenTokenInvalid_OnProtectedEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/roles");
        request.addHeader("Authorization", "invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(inMemoryToken.isTokenValid("invalid-token")).thenReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, never()).doFilter(any(), any());
        verify(inMemoryToken).isTokenValid("invalid-token");

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/problem+json"));
        assertTrue(response.getContentAsString().contains("Acesso negado"));
        assertTrue(response.getContentAsString().contains("Token ausente ou inválido"));
    }

    @Test
    void doFilterInternal_ShouldAllow_WhenTokenValid_OnProtectedEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/roles");
        request.addHeader("Authorization", "valid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(inMemoryToken.isTokenValid("valid-token")).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(inMemoryToken).isTokenValid("valid-token");
        verify(filterChain).doFilter(request, response);
        // não deve setar 401
        assertNotEquals(401, response.getStatus());
    }

    @Test
    void doFilterInternal_ShouldAllowSwaggerUiGet_WhenPublicEndpoint() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/swagger-ui/index.html");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(inMemoryToken);
    }
}
