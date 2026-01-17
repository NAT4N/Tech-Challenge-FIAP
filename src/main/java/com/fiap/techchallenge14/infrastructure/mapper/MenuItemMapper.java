package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.infrastructure.dto.MenuItemCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.MenuItemUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemMapper {
    @Mapping(source = "restaurant.id", target = "restaurantId")
    MenuItemResponseDTO toResponseDTO(MenuItemEntity menuItemEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "restaurantId", target = "restaurant.id")
    MenuItemEntity toDomain(MenuItemCreateRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    void updateDomainFromDto(MenuItemUpdateRequestDTO dto, @MappingTarget MenuItemEntity menuItemEntity);
}
