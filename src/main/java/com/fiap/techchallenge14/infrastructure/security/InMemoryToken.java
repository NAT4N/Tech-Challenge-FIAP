package com.fiap.techchallenge14.infrastructure.security;

import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.infrastructure.storage.TokenStorage;
import org.springframework.stereotype.Service;

@Service
public class InMemoryToken implements TokenMemoryPort {
    @Override
    public void saveToken(String token, Long userId) {
        TokenStorage.saveToken(token, userId);
    }

    @Override
    public boolean isTokenValid(String token) {
        return TokenStorage.isTokenValid(token);
    }
}
