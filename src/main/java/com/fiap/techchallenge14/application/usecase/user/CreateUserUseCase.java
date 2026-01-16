package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.support.UserPersistence;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final RoleRepository roleRepository;
    private final UserMapper userMapper;               // DTO <-> Domain + Domain -> ResponseDTO
    private final UserPersistence userPersistence;     // Domain <-> Entity + save/find

    @Transactional
    public UserResponseDTO execute(UserCreateRequestDTO dto) {
        User user = userMapper.toDomain(dto);

        RoleEntity role = roleRepository.findById(dto.roleId())
                .orElseThrow(() -> new UserException("Perfil nao encontrado com o ID: " + dto.roleId()));

        try {
            user.setRole(RoleType.valueOf(role.getName()));
        } catch (IllegalArgumentException e) {
            log.error("Role name {} invalid", role.getName());
            throw new UserException("Perfil inválido: " + role.getName());
        }

        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdatedAt(LocalDateTime.now());

        User saved = userPersistence.saveDomain(user);
        log.info("Usuário criado com o ID: {}", saved.getId());

        return userMapper.toResponseDTO(saved);
    }
}
