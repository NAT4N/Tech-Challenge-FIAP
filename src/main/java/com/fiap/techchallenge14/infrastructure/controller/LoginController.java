package com.fiap.techchallenge14.infrastructure.controller;

import com.fiap.techchallenge14.application.usecase.login.LoginUseCase;
import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUsecase;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO token = loginUsecase.execute(loginRequest);
        return ResponseEntity.ok(token);
    }
}

