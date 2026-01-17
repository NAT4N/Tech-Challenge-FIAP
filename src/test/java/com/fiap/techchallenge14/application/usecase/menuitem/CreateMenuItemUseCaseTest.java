package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
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
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private CreateMenuItemUseCase useCase;

    private RestaurantEntity restaurant;
    private MenuItemEntity menuItemEntity;
    private MenuItemEntity savedEntity;
    private MenuItemResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setId(1L);

        menuItemEntity = MenuItemEntity.builder()
                .name("Pizza")
                .description("Pizza de calabresa")
                .price(BigDecimal.valueOf(50.0))
                .availableOnlyInRestaurant(false)
                .photoPath("/images/pizza.jpg")
                .build();

        savedEntity = MenuItemEntity.builder()
                .id(1L)
                .name("Pizza")
                .description("Pizza de calabresa")
                .price(BigDecimal.valueOf(50.0))
                .availableOnlyInRestaurant(false)
                .photoPath("/images/pizza.jpg")
                .restaurant(restaurant)
                .build();

        responseDTO = new MenuItemResponseDTO(
                1L,
                "Pizza",
                "Pizza de calabresa",
                BigDecimal.valueOf(50.0),
                false,
                "/images/pizza.jpg",
                1L
        );
    }

    @Test
    void execute_ShouldCreateMenuItem_WhenRestaurantExists() {
        MenuItemCreateRequestDTO dto = new MenuItemCreateRequestDTO(
                "Pizza",
                "Pizza de calabresa",
                BigDecimal.valueOf(50.0),
                false,
                "/images/pizza.jpg",
                1L
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemMapper.toDomain(dto)).thenReturn(menuItemEntity);
        when(menuItemRepository.save(menuItemEntity)).thenReturn(savedEntity);
        when(menuItemMapper.toResponseDTO(savedEntity)).thenReturn(responseDTO);

        MenuItemResponseDTO result = useCase.execute(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Pizza", result.name());
        assertEquals(1L, result.restaurantId());

        verify(restaurantRepository).findById(1L);
        verify(menuItemMapper).toDomain(dto);
        verify(menuItemRepository).save(menuItemEntity);
        verify(menuItemMapper).toResponseDTO(savedEntity);
    }

    @Test
    void execute_ShouldThrowRestaurantException_WhenRestaurantNotFound() {
        MenuItemCreateRequestDTO dto = new MenuItemCreateRequestDTO(
                "Pizza",
                "Pizza de calabresa",
                BigDecimal.valueOf(50.0),
                false,
                "/images/pizza.jpg",
                1L
        );

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantException.class, () -> useCase.execute(dto));

        verify(restaurantRepository).findById(1L);
        verifyNoInteractions(menuItemMapper);
        verifyNoInteractions(menuItemRepository);
    }
}
