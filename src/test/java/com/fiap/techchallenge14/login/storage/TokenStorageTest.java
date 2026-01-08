package com.fiap.techchallenge14.login.storage;

import com.fiap.techchallenge14.infrastructure.storage.TokenStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenStorageTest {

    private final String token1 = "token-123";
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;

    @Test
    void saveToken_ShouldStoreTokenAndUserId() {
        // Act
        TokenStorage.saveToken(token1, userId1);

        // Assert
        assertTrue(TokenStorage.isTokenValid(token1));
    }

    @Test
    void isTokenValid_ShouldReturnFalse_WhenTokenNotExists() {
        // Act & Assert
        assertFalse(TokenStorage.isTokenValid("non-existent-token"));
    }

    @Test
    void saveToken_ShouldHandleMultipleTokens() {
        // Arrange & Act
        TokenStorage.saveToken(token1, userId1);
        String token2 = "token-456";
        TokenStorage.saveToken(token2, userId2);

        // Assert
        assertTrue(TokenStorage.isTokenValid(token1));
        assertTrue(TokenStorage.isTokenValid(token2));
    }

    @Test
    void saveToken_ShouldOverwriteExistingToken() {
        // Arrange
        TokenStorage.saveToken(token1, userId1);

        // Act
        TokenStorage.saveToken(token1, userId2);

        // Assert
        assertTrue(TokenStorage.isTokenValid(token1));
    }

    @Test
    void tokenStorageConstructor_Coverage() {
        new TokenStorage();
    }
}
