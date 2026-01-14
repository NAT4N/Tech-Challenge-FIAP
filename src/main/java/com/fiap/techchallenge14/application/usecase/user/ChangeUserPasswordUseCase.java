package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.support.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeUserPasswordUseCase {

    private final UserPersistence userPersistence;

    @Transactional
    public void execute(Long id, String newPassword) {
        User user = userPersistence.findDomainByIdOrThrow(id);
        user.setPassword(newPassword);

        userPersistence.saveDomain(user);
        log.info("Senha atualizada no usuário com o ID: {}", id);
    }
}
