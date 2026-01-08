package com.fiap.techchallenge14.application.usecase.login;

import com.fiap.techchallenge14.application.port.in.LoginUsecase;
import com.fiap.techchallenge14.application.port.out.TokenMemoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginUsecaseImpl implements LoginUsecase {

    private final UserRepositoryPort userRepository;
    private final TokenMemoryPort tokenMemoryPort;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = authenticate(loginRequest.login(), loginRequest.password());

        updateLastLogin(user);

        String token = generateToken(user);

        return new LoginResponseDTO(token);
    }

    private User authenticate(String username, String password) {
        return userRepository.findByLoginAndPassword(username, password)
                .filter(User::getActive)
                .orElseThrow(() -> new LoginException("Login ou senha inválidos"));
    }

    private void updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Usuário {} fez login em {}", user.getName(), user.getLastLoginAt());
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        tokenMemoryPort.saveToken(token, user.getId());
        return token;
    }
}
