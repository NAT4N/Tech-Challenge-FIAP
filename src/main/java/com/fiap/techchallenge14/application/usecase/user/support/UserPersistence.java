package com.fiap.techchallenge14.application.usecase.user.support;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistence {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public User findDomainByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toDomain)
                .orElseThrow(() -> new UserException("Usuário nao encontrado com o ID: " + id));
    }

    public User saveDomain(User user) {
        UserEntity entity = (user.getId() != null)
                ? userRepository.findById(user.getId()).orElse(new UserEntity())
                : new UserEntity();

        userEntityMapper.updateEntityFromDomain(user, entity);
        UserEntity saved = userRepository.save(entity);

        return userEntityMapper.toDomain(saved);
    }
}
