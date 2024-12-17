package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;

import java.util.List;

public interface CartService {

    CartItemResponse addToCart(Long productId, Long userId, Integer quantity);
    List<CartItemResponse> viewCart(Long userId);
}
