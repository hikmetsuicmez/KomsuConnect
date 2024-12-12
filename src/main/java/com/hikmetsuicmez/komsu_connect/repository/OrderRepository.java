package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
