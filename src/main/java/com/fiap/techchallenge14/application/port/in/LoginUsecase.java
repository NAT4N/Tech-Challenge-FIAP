package com.fiap.techchallenge14.application.port.in;

import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;

public interface LoginUsecase {
    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
