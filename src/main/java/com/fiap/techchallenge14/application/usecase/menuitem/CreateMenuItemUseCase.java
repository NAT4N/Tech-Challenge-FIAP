package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import com.fiap.techchallenge14.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    @Transactional
    public MenuItemResponseDTO execute(MenuItemCreateRequestDTO dto) {
        RestaurantEntity restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new RestaurantException("Restaurante não encontrado com o ID: " + dto.restaurantId()));

        MenuItemEntity menuItemEntity = menuItemMapper.toDomain(dto);
        menuItemEntity.setRestaurant(restaurant);

        MenuItemEntity saved = menuItemRepository.save(menuItemEntity);
        log.info("Item do cardápio criado com o ID: {} para o restaurante: {}", saved.getId(), restaurant.getId());

        return menuItemMapper.toResponseDTO(saved);
    }
}
