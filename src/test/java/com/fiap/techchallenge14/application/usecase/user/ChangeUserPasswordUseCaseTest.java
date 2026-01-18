package com.fiap.techchallenge14.application.usecase.user;

import com.fiap.techchallenge14.application.usecase.user.helper.UserPersistence;
import com.fiap.techchallenge14.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeUserPasswordUseCaseTest {

    @Mock
    private UserPersistence userPersistence;

    @InjectMocks
    private ChangeUserPasswordUseCase useCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setPassword("oldPass");
        user.setActive(true);
    }

    @Test
    void execute_ShouldUpdatePassword() {
        when(userPersistence.findDomainByIdOrThrow(1L)).thenReturn(user);

        useCase.execute(1L, "newPass");

        assertEquals("newPass", user.getPassword());
        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userPersistence).saveDomain(user);
    }

    @Test
    void execute_ShouldPropagateException_WhenUserNotFound() {
        when(userPersistence.findDomainByIdOrThrow(1L))
                .thenThrow(new RuntimeException("user not found"));

        assertThrows(RuntimeException.class, () -> useCase.execute(1L, "newPass"));

        verify(userPersistence).findDomainByIdOrThrow(1L);
        verify(userPersistence, never()).saveDomain(any());
    }
}
