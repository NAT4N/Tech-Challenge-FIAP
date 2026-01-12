package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantEntityMapperTest {

    private final RestaurantEntityMapper mapper = Mappers.getMapper(RestaurantEntityMapper.class);

    @Test
    void toDomain_ShouldMapCorrectly() {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Teste");

        RestaurantEntity result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        RestaurantEntity domain = new RestaurantEntity();
        domain.setId(1L);
        domain.setName("Teste");

        RestaurantEntity result = mapper.toEntity(domain);

        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getName(), result.getName());
    }

    @Test
    void toDomain_WithNull_ShouldReturnNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_WithNull_ShouldReturnNull() {
        assertNull(mapper.toEntity(null));
    }
}
