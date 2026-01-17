package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    RestaurantResponseDTO toResponseDTO(RestaurantEntity restaurantEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "ownerId", target = "owner.id")
    RestaurantEntity toDomain(RestaurantCreateRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateDomainFromDto(RestaurantUpdateRequestDTO dto, @MappingTarget RestaurantEntity restaurantEntity);
}
