package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.helper.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserPersistence userPersistence;

    @Transactional
    public UserResponseDTO execute(Long id, UserUpdateRequestDTO dto) {
        User user = userPersistence.findDomainByIdOrThrow(id);

        userRepository.findByEmail(dto.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserException("E-mail já está em uso");
                });

        userRepository.findByLogin(dto.login())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserException("Login já está em uso");
                });

        userMapper.updateDomainFromDto(dto, user);

        user.setLastUpdatedAt(LocalDateTime.now());

        User updated = userPersistence.saveDomain(user);
        log.info("Usuário atualizado com o ID: {}", updated.getId());

        return userMapper.toResponseDTO(updated);
    }
}
