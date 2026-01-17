package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindMenuItemsByRestaurantUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private FindMenuItemsByRestaurantUseCase useCase;

    @Test
    void execute_ShouldReturnList_WhenRestaurantHasItems() {
        MenuItemEntity entity = MenuItemEntity.builder().id(1L).name("Pizza").build();
        MenuItemResponseDTO responseDTO = new MenuItemResponseDTO(1L, "Pizza", "Desc", BigDecimal.TEN, false, "path", 1L);

        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(entity));
        when(menuItemMapper.toResponseDTO(entity)).thenReturn(responseDTO);

        List<MenuItemResponseDTO> result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        verify(menuItemRepository).findByRestaurantId(1L);
    }
}
