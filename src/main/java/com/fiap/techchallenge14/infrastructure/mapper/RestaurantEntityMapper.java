package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantEntityMapper {

    RestaurantEntity toDomain(RestaurantEntity entity);

    RestaurantEntity toEntity(RestaurantEntity domain);
}
