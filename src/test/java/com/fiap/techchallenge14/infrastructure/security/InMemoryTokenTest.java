package com.fiap.techchallenge14.infrastructure.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTokenTest {

    private final InMemoryToken inMemoryToken = new InMemoryToken();

    @Test
    void saveTokenAndIsTokenValid_ShouldWork() {
        String token = "test-token";
        Long userId = 1L;

        inMemoryToken.saveToken(token, userId);

        assertTrue(inMemoryToken.isTokenValid(token));
    }

    @Test
    void isTokenValid_WithInvalidToken_ShouldReturnFalse() {
        assertFalse(inMemoryToken.isTokenValid("invalid-token"));
    }
}
