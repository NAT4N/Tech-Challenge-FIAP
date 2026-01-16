package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleException("Tipo de usuário não encontrado com o ID: " + id);
        }

        long usersUsingRole = userRepository.countByRoleId(id);
        if (usersUsingRole > 0) {
            throw new RoleException("Não é possível deletar. Existem " + usersUsingRole + " usuário(s) usando esse tipo.");
        }

        roleRepository.deleteById(id);
        log.info("Tipo de usuário deletado com ID: {}", id);
    }
}
