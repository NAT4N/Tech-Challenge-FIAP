package com.fiap.techchallenge14.application.usecase.user.support;

import com.fiap.techchallenge14.domain.model.User;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistence {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public User findDomainByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain)
                .orElseThrow(() ->
                        new UserException("Usuário não encontrado com o ID: " + id)
                );
    }

    public User saveDomain(User user) {

        UserEntity entity = (user.getId() != null)
                ? userRepository.findById(user.getId()).orElse(new UserEntity())
                : new UserEntity();

        userMapper.updateEntityFromDomain(user, entity);

        RoleEntity role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() ->
                        new UserException("Perfil não encontrado com o ID: " + user.getRoleId())
                );

        entity.setRole(role);

        UserEntity saved = userRepository.save(entity);
        return userMapper.toDomain(saved);
    }
}
