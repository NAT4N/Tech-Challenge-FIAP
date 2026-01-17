package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.mapper.MenuItemMapper;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @Transactional
    public MenuItemResponseDTO execute(Long id, MenuItemUpdateRequestDTO dto) {
        MenuItemEntity menuItemEntity = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemException("Item do cardápio não encontrado com o ID: " + id));

        menuItemMapper.updateDomainFromDto(dto, menuItemEntity);

        MenuItemEntity updated = menuItemRepository.save(menuItemEntity);
        log.info("Item do cardápio atualizado com o ID: {}", updated.getId());

        return menuItemMapper.toResponseDTO(updated);
    }
}
