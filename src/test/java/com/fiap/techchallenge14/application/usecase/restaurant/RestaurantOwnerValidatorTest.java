package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.entity.RoleEntity;
import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantOwnerValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestaurantOwnerValidator validator;

    @Test
    void loadRestaurantOwnerOrThrow_ShouldReturnUser_WhenUserHasRestaurantOwnerRole() {
        // given
        RoleEntity role = new RoleEntity();
        role.setName("RESTAURANT_OWNER");

        UserEntity user = new UserEntity();
        user.setId(10L);
        user.setRole(role);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        // when
        UserEntity result = validator.loadRestaurantOwnerOrThrow(10L);

        // then
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("RESTAURANT_OWNER", result.getRole().getName());

        verify(userRepository).findById(10L);
    }

    @Test
    void loadRestaurantOwnerOrThrow_ShouldThrowException_WhenUserNotFound() {
        // given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when / then
        RestaurantException ex =
                assertThrows(RestaurantException.class,
                        () -> validator.loadRestaurantOwnerOrThrow(99L));

        assertTrue(ex.getMessage().contains("Proprietário não encontrado com o ID: 99"));

        verify(userRepository).findById(99L);
    }

    @Test
    void loadRestaurantOwnerOrThrow_ShouldThrowException_WhenUserDoesNotHaveRole() {
        // given
        UserEntity user = new UserEntity();
        user.setId(20L);
        user.setRole(null);

        when(userRepository.findById(20L)).thenReturn(Optional.of(user));

        // when / then
        RestaurantException ex =
                assertThrows(RestaurantException.class,
                        () -> validator.loadRestaurantOwnerOrThrow(20L));

        assertTrue(ex.getMessage().contains("Usuário 20 não possui a role RESTAURANT_OWNER"));

        verify(userRepository).findById(20L);
    }

    @Test
    void loadRestaurantOwnerOrThrow_ShouldThrowException_WhenUserHasDifferentRole() {
        // given
        RoleEntity role = new RoleEntity();
        role.setName("CLIENT");

        UserEntity user = new UserEntity();
        user.setId(30L);
        user.setRole(role);

        when(userRepository.findById(30L)).thenReturn(Optional.of(user));

        // when / then
        RestaurantException ex =
                assertThrows(RestaurantException.class,
                        () -> validator.loadRestaurantOwnerOrThrow(30L));

        assertTrue(ex.getMessage().contains("Usuário 30 não possui a role RESTAURANT_OWNER"));

        verify(userRepository).findById(30L);
    }
}
