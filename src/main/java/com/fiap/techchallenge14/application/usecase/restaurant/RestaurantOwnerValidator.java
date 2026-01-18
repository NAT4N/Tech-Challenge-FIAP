package com.fiap.techchallenge14.application.usecase.restaurant;

import com.fiap.techchallenge14.infrastructure.entity.UserEntity;
import com.fiap.techchallenge14.infrastructure.exception.RestaurantException;
import com.fiap.techchallenge14.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantOwnerValidator {

    private static final String RESTAURANT_OWNER = "RESTAURANT_OWNER";

    private final UserRepository userRepository;

    public UserEntity loadRestaurantOwnerOrThrow(Long ownerId) {
        UserEntity user = userRepository.findById(ownerId)
                .orElseThrow(() -> new RestaurantException("Proprietário não encontrado com o ID: " + ownerId));

        String roleName = user.getRole() != null ? user.getRole().getName() : null;

        if (!RESTAURANT_OWNER.equals(roleName)) {
            throw new RestaurantException("Usuário " + ownerId + " não possui a role RESTAURANT_OWNER.");
        }

        return user;
    }
}
