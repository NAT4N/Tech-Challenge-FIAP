package com.fiap.techchallenge14.infrastructure.repository;

import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findByNameContainingIgnoreCase(String name);

    Optional<UserEntity> findByLoginAndPassword(String login, String password);
}
