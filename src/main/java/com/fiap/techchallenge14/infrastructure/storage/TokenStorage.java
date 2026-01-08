package com.fiap.techchallenge14.infrastructure.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStorage {

    private static final Map<String, Long> tokenUserMap = new ConcurrentHashMap<>();

    public static void saveToken(String token, Long userId) {
        tokenUserMap.put(token, userId);
    }

    public static boolean isTokenValid(String token) {
        return tokenUserMap.containsKey(token);
    }
}
