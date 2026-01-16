package com.fiap.techchallenge14.application.usecase.login;

import com.fiap.techchallenge14.domain.dto.LoginRequestDTO;
import com.fiap.techchallenge14.domain.dto.LoginResponseDTO;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.exception.LoginException;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
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

    private final UserRepository userRepository;     // JPA repo (UserEntity)
    private final UserEntityMapper userEntityMapper; // Entity <-> Domain
    private final InMemoryToken inMemoryToken;       // service concreto

    @Transactional
    public LoginResponseDTO execute(LoginRequestDTO loginRequest) {
        User user = authenticate(loginRequest.login(), loginRequest.password());

        updateLastLogin(user);

        String token = generateToken(user);

        return new LoginResponseDTO(token);
    }

    private User authenticate(String username, String password) {
        var userEntity = userRepository.findByLoginAndPassword(username, password)
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .orElseThrow(() -> new LoginException("Login ou senha inválidos"));

        return userEntityMapper.toDomain(userEntity);
    }

    private void updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());

        var entity = userEntityMapper.toEntity(user);
        userRepository.save(entity);

        log.info("Usuário {} fez login em {}", user.getName(), user.getLastLoginAt());
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        inMemoryToken.saveToken(token, user.getId());
        return token;
    }
}
