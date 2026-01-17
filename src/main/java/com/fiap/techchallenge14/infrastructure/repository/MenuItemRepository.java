package com.fiap.techchallenge14.infrastructure.repository;

import com.fiap.techchallenge14.infrastructure.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    List<MenuItemEntity> findByRestaurantId(Long restaurantId);
}
