package com.fiap.techchallenge14.application.usecase.login;

import com.fiap.techchallenge14.infrastructure.dto.LoginRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.LoginResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import com.fiap.techchallenge14.infrastructure.security.InMemoryToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final InMemoryToken inMemoryToken;

    @Transactional
    public LoginResponseDTO execute(LoginRequestDTO loginRequest) {
        UserEntity userEntity = authenticateEntity(loginRequest.login(), loginRequest.password());

        userEntity.setLastLoginAt(LocalDateTime.now());
        userRepository.save(userEntity);

        String token = UUID.randomUUID().toString();
        inMemoryToken.saveToken(token, userEntity.getId());

        log.info("Usuário {} fez login em {}", userEntity.getName(), userEntity.getLastLoginAt());
        return new LoginResponseDTO(token);
    }

    private UserEntity authenticateEntity(String username, String password) {
        return userRepository.findByLoginAndPassword(username, password)
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .orElseThrow(() -> new LoginException("Login ou senha inválidos"));
    }
}
