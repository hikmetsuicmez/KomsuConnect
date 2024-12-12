package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.OrderRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import com.hikmetsuicmez.komsu_connect.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController extends RestBaseController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request.getUserId());
        return ApiResponse.success(order);
    }

    @GetMapping("/history")
    public ApiResponse<?> getOrderHistory(@RequestParam Long userId) {
        List<OrderResponse> orderHistory = orderService.getOrderHistory(userId);
        return ApiResponse.success(orderHistory);
    }
}
