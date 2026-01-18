package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.helper.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserMapper userMapper;
    private final UserPersistence userPersistence;

    @Transactional
    public UserResponseDTO execute(UserCreateRequestDTO dto) {
        User user = userMapper.toDomain(dto);

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdatedAt(LocalDateTime.now());

        User saved = userPersistence.saveDomain(user);

        log.info("Usuário criado com o ID: {}", saved.getId());

        return userMapper.toResponseDTO(saved);
    }
}
