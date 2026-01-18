package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.helper.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private UserPersistence userPersistence;

    @InjectMocks
    private DeleteUserUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setActive(true);
    }

    @Test
    void execute_ShouldDeactivateUser_WhenActive() {
        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);

        useCase.execute(1L);

        assertFalse(user.getActive(), "deve desativar o usuário (delete lógico)");
        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userPersistence).saveDomain(user);
    }

    @Test
    void execute_ShouldDoNothing_WhenAlreadyInactive() {
        user.setActive(false);
        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);

        useCase.execute(1L);

        assertFalse(user.getActive());
        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userPersistence, never()).saveDomain(any(User.class));
    }

    @Test
    void execute_ShouldPropagateException_WhenUserNotFound() {
        when(userPersistence.findDomainByIdOrThrow(1L))
                .thenThrow(new RuntimeException("user not found"));

        assertThrows(RuntimeException.class, () -> useCase.execute(1L));

        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userPersistence, never()).saveDomain(any());
    }
}
