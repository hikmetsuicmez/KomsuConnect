package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByProductIdAndUserId(Long productId, Long userId);
}
