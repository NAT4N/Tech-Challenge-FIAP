package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class RoleEntityMapperTest {

    private final RoleEntityMapper mapper = Mappers.getMapper(RoleEntityMapper.class);

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("CLIENT");

        Role domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("CLIENT", domain.getName());
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        Role domain = new Role();
        domain.setId(2L);
        domain.setName("ADMIN");

        RoleEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("ADMIN", entity.getName());
    }

    @Test
    void toDomain_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
