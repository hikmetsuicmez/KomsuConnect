package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.CartItem;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.CartItemRequest;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;

public class CartItemMapper {

    public static CartItemResponse toResponseDto(CartItem cartItem) {
        return CartItemResponse.builder()
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public static CartItem toEntity(CartItemRequest cartItemRequest, User user, Product product) {
        return CartItem.builder()
                .product(product)
                .quantity(cartItemRequest.getQuantity())
                .user(user)
                .build();
    }
}
