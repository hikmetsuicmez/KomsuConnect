package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.Order;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.OrderRequest;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderMapper {

        public static OrderResponse toResponseDto(Order order) {
                OrderResponse dto = new OrderResponse();
                dto.setOrderId(order.getId());
                dto.setTotalPrice(order.getTotalPrice());
                dto.setCreatedAt(order.getCreatedAt());

                if (order.getOrderItems() != null) {
                        log.info("OrderItems size: {}", order.getOrderItems().size());
                        dto.setOrderItems(order.getOrderItems().stream()
                                    .map(OrderItemMapper::toResponseDto)
                                    .toList());
                } else {
                        log.warn("OrderItems is null for Order ID: {}", order.getId());
                }
                return dto;
        }

        public static Order toEntity(OrderRequest orderRequest, User user) {
                Order order = new Order();
                order.setUser(user);
                order.setCreatedAt(orderRequest.getCreatedAt());
                return order;
        }
}
