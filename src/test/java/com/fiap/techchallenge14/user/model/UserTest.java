package com.fiap.techchallenge14.user.model;
/*
import com.fiap.techchallenge14.role.model.Role;
import com.fiap.techchallenge14.role.model.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private User user;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleType.CLIENT);

        user = new User();
        user.setName("Teste");
        user.setEmail("teste@mail.com");
        user.setLogin("login");
        user.setRole(role);
    }

    @Test
    void prePersist_ShouldSetCreatedAtLastUpdatedAndActive() {
        // Act
        user.prePersist();

        // Assert
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastUpdatedAt());
        assertTrue(user.getActive());
    }

    @Test
    void preUpdate_ShouldUpdateLastUpdated() {
        // Arrange
        user.setLastUpdatedAt(LocalDateTime.now().minusDays(1));

        // Act
        user.preUpdate();

        // Assert
        assertNotNull(user.getLastUpdatedAt());
        assertTrue(user.getLastUpdatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }
}*/
