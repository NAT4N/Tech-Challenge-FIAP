package com.fiap.techchallenge14.application.usecase.menuitem;

import com.fiap.techchallenge14.infrastructure.exception.MenuItemException;
import com.fiap.techchallenge14.infrastructure.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMenuItemUseCase {

    private final MenuItemRepository menuItemRepository;

    @Transactional
    public void execute(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemException("Item do cardápio não encontrado com o ID: " + id);
        }
        menuItemRepository.deleteById(id);
        log.info("Item do cardápio deletado com o ID: {}", id);
    }
}
