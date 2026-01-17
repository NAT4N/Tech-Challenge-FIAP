package com.fiap.techchallenge14.infrastructure.controller;

import com.fiap.techchallenge14.application.usecase.menuitem.*;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Gerenciamento de itens do cardápio")
@SecurityRequirement(name = "Authorization")
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase;

    @PostMapping
    @Operation(summary = "Cria um novo item no cardápio")
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @Valid @RequestBody MenuItemCreateRequestDTO dto
    ) {
        MenuItemResponseDTO created = createMenuItemUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualiza parcialmente um item do cardápio")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemUpdateRequestDTO dto
    ) {
        MenuItemResponseDTO updated = updateMenuItemUseCase.execute(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um item do cardápio")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um item do cardápio pelo ID")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemResponseDTO menuItem = findMenuItemByIdUseCase.execute(id);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Lista todos os itens do cardápio de um restaurante")
    public ResponseEntity<List<MenuItemResponseDTO>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemResponseDTO> menuItems = findMenuItemsByRestaurantUseCase.execute(restaurantId);
        return ResponseEntity.ok(menuItems);
    }
}
