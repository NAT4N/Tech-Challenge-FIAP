package com.fiap.techchallenge14.application.port.out;

public interface TokenMemoryPort {
    void saveToken(String token, Long userId);
    boolean isTokenValid(String token);
}
