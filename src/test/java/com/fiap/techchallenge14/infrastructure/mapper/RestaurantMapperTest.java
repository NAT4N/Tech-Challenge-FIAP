package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.infrastructure.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.infrastructure.dto.RestaurantUpdateRequestDTO;
import com.fiap.techchallenge14.infrastructure.entity.RestaurantEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantMapperTest {

    private final RestaurantMapper mapper = Mappers.getMapper(RestaurantMapper.class);

    @Test
    void toResponseDTO_ShouldMapCorrectly() {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setName("Restaurante Teste");
        UserEntity owner = new UserEntity();
        owner.setId(10L);
        owner.setName("Owner Name");
        entity.setOwner(owner);

        RestaurantResponseDTO response = mapper.toResponseDTO(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.id());
        assertEquals(entity.getName(), response.name());
        assertEquals(10L, response.ownerId());
        assertEquals("Owner Name", response.ownerName());
    }

    @Test
    void toDomain_ShouldMapCorrectly() {
        RestaurantCreateRequestDTO dto = new RestaurantCreateRequestDTO(
                "Restaurante Teste", "Endereco", "Culinaria", "10:00-22:00", 10L
        );

        RestaurantEntity entity = mapper.toDomain(dto);

        assertNotNull(entity);
        assertEquals(dto.name(), entity.getName());
        assertEquals(10L, entity.getOwner().getId());
    }

    @Test
    void updateDomainFromDto_ShouldUpdateCorrectly() {
        RestaurantEntity entity = new RestaurantEntity();
        entity.setName("Old Name");

        RestaurantUpdateRequestDTO dto = new RestaurantUpdateRequestDTO(
                "New Name", "New Endereco", "New Culinaria", "09:00-21:00", 20L
        );

        mapper.updateDomainFromDto(dto, entity);

        assertEquals(dto.name(), entity.getName());
        assertEquals(20L, entity.getOwner().getId());
    }

    @Test
    void toResponseDTO_WithNull_ShouldReturnNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toDomain_WithNull_ShouldReturnNull() {
        assertNull(mapper.toDomain(null));
    }
}
