package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.domain.dto.UserResponseDTO;
import com.fiap.techchallenge14.infrastructure.exception.UserException;
import com.fiap.techchallenge14.infrastructure.mapper.UserEntityMapper;
import com.fiap.techchallenge14.infrastructure.mapper.UserMapper;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUsersUseCase {

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper; // Entity -> Domain
    private final UserMapper userMapper;             // Domain -> ResponseDTO

    @Transactional(readOnly = true)
    public List<UserResponseDTO> execute(String name) {
        var entities = (name == null || name.trim().isEmpty())
                ? userRepository.findAll()
                : userRepository.findByNameContainingIgnoreCase(name);

        var users = entities.stream()
                .map(userEntityMapper::toDomain)
                .map(userMapper::toResponseDTO)
                .toList();

        if (users.isEmpty()) {
            throw new UserException("Usuário nao encontrado com o nome: " + name);
        }

        return users;
    }
}
