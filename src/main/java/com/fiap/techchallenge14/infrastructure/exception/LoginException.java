package com.fiap.techchallenge14.infrastructure.exception;

public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}
