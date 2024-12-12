package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
