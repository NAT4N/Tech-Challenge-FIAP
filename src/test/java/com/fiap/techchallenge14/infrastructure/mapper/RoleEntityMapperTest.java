package com.fiap.techchallenge14.infrastructure.mapper;

import com.fiap.techchallenge14.domain.model.Role;
import com.fiap.techchallenge14.domain.model.RoleType;
import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEntityMapperTest {

    @Test
    void toDomain_ShouldMapCorrectly() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1L);
        entity.setName("CLIENT");

        Role domain = RoleEntityMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals("CLIENT", domain.getName());
        assertEquals("CLIENT", domain.getType());
    }

    @Test
    void toDomain_WithInvalidName_ShouldHandleGracefully() {
        RoleEntity entity = new RoleEntity();
        entity.setName("INVALID");

        Role domain = RoleEntityMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals("INVALID", domain.getName());
        assertNull(domain.getType());
    }

    @Test
    void toDomain_WithNull_ShouldReturnNull() {
        assertNull(RoleEntityMapper.toDomain(null));
    }

    @Test
    void toEntity_ShouldMapCorrectlyFromType() {
        Role domain = new Role();
        domain.setId(1L);
        domain.setType("CLIENT");

        RoleEntity entity = RoleEntityMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals("CLIENT", entity.getName());
    }

    @Test
    void toEntity_ShouldMapCorrectlyFromNameIfTypeNull() {
        Role domain = new Role();
        domain.setName("RESTAURANT_OWNER");

        RoleEntity entity = RoleEntityMapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals("RESTAURANT_OWNER", entity.getName());
    }

    @Test
    void toEntity_WithInvalidType_ShouldHandleGracefully() {
        Role domain = new Role();
        domain.setType("INVALID");

        RoleEntity entity = RoleEntityMapper.toEntity(domain);

        assertNotNull(entity);
        assertNull(entity.getName());
    }

    @Test
    void toEntity_WithNull_ShouldReturnNull() {
        assertNull(RoleEntityMapper.toEntity(null));
    }
}
