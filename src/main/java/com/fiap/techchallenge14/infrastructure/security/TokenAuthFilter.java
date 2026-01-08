package com.fiap.techchallenge14.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String USERS_ENDPOINT = "/v1/users";
    private static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/v1/login", "/v3/api-docs", "/swagger-ui",
            "/webjars", "/swagger-resources"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenMemoryPort tokenMemoryPort;

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (isPublicEndpoint(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader(AUTH_HEADER);

       /* if (token == null || !tokenMemoryPort.isTokenValid(token)) {
            ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
            problem.setTitle("Acesso negado");
            problem.setType(URI.create("/problems/unauthorized"));
            problem.setDetail("Token ausente ou inválido. Verifique o cabeçalho 'Authorization' e tente novamente.");
            problem.setInstance(URI.create(request.getRequestURI()));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/problem+json;charset=UTF-8");
            objectMapper.writeValue(response.getWriter(), problem);
            return;
        }*/

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        if (path.equals(USERS_ENDPOINT) && method.equalsIgnoreCase(HttpMethod.POST.name()))
            return true;

        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith)
                && (method.equalsIgnoreCase(HttpMethod.POST.name())
                || method.equalsIgnoreCase(HttpMethod.GET.name()));
    }
}
