package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.port.in.UserUsecase;
import com.fiap.techchallenge14.application.port.out.RoleRepositoryPort;
import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.domain.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.domain.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserUsecaseImpl implements UserUsecase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO save(UserCreateRequestDTO dto) {
        User user = userMapper.toDomain(dto);

        RoleEntity role = roleRepository.findEntityById(dto.roleId())
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

        User savedUser = userRepository.save(user);
        log.info("Usuário criado com o ID: {}", savedUser.getId());

        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO dto) {
        User user = getUserById(id);

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

        RoleEntity role = roleRepository.findEntityById(dto.roleId())
                .orElseThrow(() -> new UserException("Perfil nao encontrado com o ID: " + dto.roleId()));

        try {
            user.setRole(RoleType.valueOf(role.getName()));
        } catch (IllegalArgumentException e) {
            log.error("Role name {} invalid", role.getName());
            throw new UserException("Perfil inválido: " + role.getName());
        }

        User updatedUser = userRepository.save(user);
        log.info("Usuário atualizado com o ID: {}", updatedUser.getId());

        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = getUserById(id);

        if (Boolean.FALSE.equals(user.getActive())) {
            log.warn("Esse usuário já está inativo, ID: {}", id);
            return;
        }

        user.setActive(false);
        userRepository.save(user);

        log.info("Usuário deletado logicamente com o ID: {}", id);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Usuário nao encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findUsers(String name) {
        List<User> domainUserEntities;

        if (name == null || name.trim().isEmpty()) {
            domainUserEntities = userRepository.findAll();
        } else {
            domainUserEntities = userRepository.findByNameContainingIgnoreCase(name);
        }

        List<UserResponseDTO> users = domainUserEntities.stream()
                .map(userMapper::toResponseDTO)
                .toList();

        if (users.isEmpty()) {
            throw new UserException("Usuário nao encontrado com o nome: " + name);
        }

        return users;
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(newPassword);

        userRepository.save(user);
        log.info("Senha atualizada no usuário com o ID: {}", id);
    }
}
