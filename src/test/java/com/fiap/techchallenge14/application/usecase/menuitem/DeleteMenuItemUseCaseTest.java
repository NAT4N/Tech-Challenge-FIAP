package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private DeleteMenuItemUseCase useCase;

    @Test
    void execute_ShouldDeleteMenuItem_WhenItemExists() {
        when(menuItemRepository.existsById(1L)).thenReturn(true);

        useCase.execute(1L);

        verify(menuItemRepository).existsById(1L);
        verify(menuItemRepository).deleteById(1L);
    }

    @Test
    void execute_ShouldThrowMenuItemException_WhenItemDoesNotExist() {
        when(menuItemRepository.existsById(1L)).thenReturn(false);

        assertThrows(MenuItemException.class, () -> useCase.execute(1L));

        verify(menuItemRepository).existsById(1L);
        verify(menuItemRepository, never()).deleteById(anyLong());
    }
}
