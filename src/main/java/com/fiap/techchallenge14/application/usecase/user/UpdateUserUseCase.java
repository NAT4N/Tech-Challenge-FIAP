package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.support.UserPersistence;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;   // pra validar email/login sem converter tudo
    private final RoleRepository roleRepository;
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

        RoleEntity role = roleRepository.findById(dto.roleId())
                .orElseThrow(() -> new UserException("Perfil nao encontrado com o ID: " + dto.roleId()));

        try {
            user.setRole(RoleType.valueOf(role.getName()));
        } catch (IllegalArgumentException e) {
            log.error("Role name {} invalid", role.getName());
            throw new UserException("Perfil inválido: " + role.getName());
        }

        User updated = userPersistence.saveDomain(user);
        log.info("Usuário atualizado com o ID: {}", updated.getId());

        return userMapper.toResponseDTO(updated);
    }
}
