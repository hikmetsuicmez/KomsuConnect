package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.Order;
import com.hikmetsuicmez.komsu_connect.entity.OrderItem;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.response.OrderItemResponse;

public class OrderItemMapper {

    public static OrderItemResponse toResponseDto(OrderItem orderItem) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        return dto;
    }

    public static OrderItem toEntity(OrderItemResponse orderItemResponseDTO, Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemResponseDTO.getQuantity());
        orderItem.setPrice(orderItemResponseDTO.getPrice());
        return orderItem;
    }
}
