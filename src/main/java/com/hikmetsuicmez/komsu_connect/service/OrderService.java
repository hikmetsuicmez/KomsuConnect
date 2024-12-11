package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(Long userId);
    List<OrderResponse> getOrderHistory(Long userId);
}
