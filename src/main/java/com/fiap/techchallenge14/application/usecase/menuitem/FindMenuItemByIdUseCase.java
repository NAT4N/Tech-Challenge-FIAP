package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindMenuItemByIdUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemResponseDTO execute(Long id) {
        return menuItemRepository.findById(id)
                .map(menuItemMapper::toResponseDTO)
                .orElseThrow(() -> new MenuItemException("Item do cardápio não encontrado com o ID: " + id));
    }
}
