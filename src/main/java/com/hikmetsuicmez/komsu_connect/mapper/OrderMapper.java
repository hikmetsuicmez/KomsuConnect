package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.Order;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.OrderRequest;
import com.hikmetsuicmez.komsu_connect.response.OrderItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponseDto(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setOrderId(order.getId());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());
        List<OrderItemResponse> orderItems = order.getOrderItems().stream().map(OrderItemMapper::toResponseDto).toList();
        dto.setOrderItems(orderItems);
        return dto;
    }

    public static Order toEntity(OrderRequest orderRequest, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(orderRequest.getCreatedAt());
        return order;
    }
}
