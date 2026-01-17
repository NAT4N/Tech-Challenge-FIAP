package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindMenuItemsByRestaurantUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public List<MenuItemResponseDTO> execute(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(menuItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
