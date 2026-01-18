package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.helper.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

    private final UserPersistence userPersistence;

    @Transactional
    public void execute(Long id) {
        User user = userPersistence.findDomainByIdOrThrow(id);

        if (Boolean.FALSE.equals(user.getActive())) {
            log.warn("Esse usuário já está inativo, ID: {}", id);
            return;
        }

        user.setActive(false);
        userPersistence.saveDomain(user);

        log.info("Usuário deletado logicamente com o ID: {}", id);
    }
}
