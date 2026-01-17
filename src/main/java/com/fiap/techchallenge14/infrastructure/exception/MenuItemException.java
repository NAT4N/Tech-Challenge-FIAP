package com.fiap.techchallenge14.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MenuItemException extends RuntimeException {
    public MenuItemException(String message) {
        super(message);
    }
}
