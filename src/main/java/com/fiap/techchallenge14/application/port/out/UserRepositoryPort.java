package com.fiap.techchallenge14.application.port.out;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    Optional<User> findByLoginAndPassword(String login, String password);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findAll();
    User save(User user);
    User update(Long id, User user);
}
