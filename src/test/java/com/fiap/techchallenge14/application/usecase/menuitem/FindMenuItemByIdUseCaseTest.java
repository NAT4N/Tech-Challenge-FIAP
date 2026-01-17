package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private FindMenuItemByIdUseCase useCase;

    @Test
    void execute_ShouldReturnMenuItem_WhenExists() {
        MenuItemEntity entity = MenuItemEntity.builder().id(1L).name("Pizza").build();
        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO(1L, "Pizza", "Desc", BigDecimal.TEN, false, "path", 1L);

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(menuItemMapper.toResponseDTO(entity)).thenReturn(responseDTO);

        MenuItemResponseDTO result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(menuItemRepository).findById(1L);
    }

    @Test
    void execute_ShouldThrowMenuItemException_WhenDoesNotExist() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MenuItemException.class, () -> useCase.execute(1L));

        verify(menuItemRepository).findById(1L);
    }
}
