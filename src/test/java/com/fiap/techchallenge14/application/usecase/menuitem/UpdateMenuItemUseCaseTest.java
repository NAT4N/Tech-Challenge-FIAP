package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
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
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private UpdateMenuItemUseCase useCase;

    private MenuItemEntity menuItemEntity;
    private MenuItemResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        menuItemEntity = MenuItemEntity.builder()
                .id(1L)
                .name("Pizza")
                .description("Pizza de calabresa")
                .price(BigDecimal.valueOf(50.0))
                .availableOnlyInRestaurant(false)
                .photoPath("/images/pizza.jpg")
                .build();

        responseDTO = new MenuItemResponseDTO(
                1L,
                "Pizza Atualizada",
                "Descrição Atualizada",
                BigDecimal.valueOf(60.0),
                true,
                "/images/pizza_nova.jpg",
                1L
        );
    }

    @Test
    void execute_ShouldUpdateMenuItem_WhenItemExists() {
        MenuItemUpdateRequestDTO dto = new MenuItemUpdateRequestDTO(
                "Pizza Atualizada",
                "Descrição Atualizada",
                BigDecimal.valueOf(60.0),
                true,
                "/images/pizza_nova.jpg"
        );

        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItemEntity));
        when(menuItemRepository.save(menuItemEntity)).thenReturn(menuItemEntity);
        when(menuItemMapper.toResponseDTO(menuItemEntity)).thenReturn(responseDTO);

        MenuItemResponseDTO result = useCase.execute(1L, dto);

        assertNotNull(result);
        assertEquals("Pizza Atualizada", result.name());
        verify(menuItemRepository).findById(1L);
        verify(menuItemMapper).updateDomainFromDto(dto, menuItemEntity);
        verify(menuItemRepository).save(menuItemEntity);
    }

    @Test
    void execute_ShouldThrowMenuItemException_WhenItemNotFound() {
        MenuItemUpdateRequestDTO dto = new MenuItemUpdateRequestDTO(
                "Pizza Atualizada",
                "Descrição Atualizada",
                BigDecimal.valueOf(60.0),
                true,
                "/images/pizza_nova.jpg"
        );

        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MenuItemException.class, () -> useCase.execute(1L, dto));

        verify(menuItemRepository).findById(1L);
        verifyNoMoreInteractions(menuItemMapper);
        verifyNoMoreInteractions(menuItemRepository);
    }
}
