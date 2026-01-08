package com.fiap.techchallenge14.application.adapters;

import com.fiap.techchallenge14.application.port.out.UserRepositoryPort;
import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserEntityMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password).map(mapper::toDomain);
    }

    @Override
    public List<User> findByNameContainingIgnoreCase(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserEntity entity;
        if (user.getId() != null) {
            entity = userRepository.findById(user.getId()).orElse(new UserEntity());
        } else {
            entity = new UserEntity();
        }

        mapper.updateEntityFromDomain(user, entity);

        if (user.getRole() != null) {
            RoleEntity role = roleRepository.findByName(user.getRole().name())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + user.getRole().name()));
            entity.setRole(role);
        }

        var saved = userRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public User update(Long id, User user) {
        user.setId(id);
        return save(user);
    }
}
