package com.fiap.techchallenge14.infrastructure.controller;

import com.fiap.techchallenge14.application.port.in.RestaurantUsecase;
import com.fiap.techchallenge14.domain.dto.RestaurantCreateRequestDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantResponseDTO;
import com.fiap.techchallenge14.domain.dto.RestaurantUpdateRequestDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/restaurants")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class RestaurantController {

    private final RestaurantUsecase restaurantUsecase;

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(@Valid @RequestBody RestaurantCreateRequestDTO restaurantRequestDTO) {
        RestaurantResponseDTO createdRestaurant = restaurantUsecase.save(restaurantRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateRequestDTO restaurantRequestDTO) {

        RestaurantResponseDTO updatedRestaurant = restaurantUsecase.update(id, restaurantRequestDTO);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantUsecase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        List<RestaurantResponseDTO> restaurants = restaurantUsecase.findAll();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Long id) {
        RestaurantResponseDTO restaurant = restaurantUsecase.findById(id);
        return ResponseEntity.ok(restaurant);
    }
}
