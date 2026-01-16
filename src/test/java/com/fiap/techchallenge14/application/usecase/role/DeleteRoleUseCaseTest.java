package com.fiap.techchallenge14.application.usecase.role;

import com.fiap.techchallenge14.infrastructure.exception.RoleException;
import com.fiap.techchallenge14.infrastructure.repository.RoleRepository;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteRoleUseCaseTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DeleteRoleUseCase useCase;

    @Test
    void execute_ShouldDeleteRole_WhenExistsAndNoUsersUsingIt() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        when(userRepository.countByRoleId(1L)).thenReturn(0L);

        assertDoesNotThrow(() -> useCase.execute(1L));

        verify(roleRepository).existsById(1L);
        verify(userRepository).countByRoleId(1L);
        verify(roleRepository).deleteById(1L);
    }

    @Test
    void execute_ShouldThrowRoleException_WhenRoleDoesNotExist() {
        when(roleRepository.existsById(1L)).thenReturn(false);

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(1L));
        assertTrue(ex.getMessage().contains("Tipo de usuário não encontrado com o ID: 1"));

        verify(roleRepository).existsById(1L);
        verifyNoInteractions(userRepository);
        verify(roleRepository, never()).deleteById(anyLong());
    }

    @Test
    void execute_ShouldThrowRoleException_WhenUsersAreUsingRole() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        when(userRepository.countByRoleId(1L)).thenReturn(3L);

        RoleException ex = assertThrows(RoleException.class, () -> useCase.execute(1L));
        assertEquals("Não é possível deletar. Existem 3 usuário(s) usando esse tipo.", ex.getMessage());

        verify(roleRepository).existsById(1L);
        verify(userRepository).countByRoleId(1L);
        verify(roleRepository, never()).deleteById(anyLong());
    }
}
