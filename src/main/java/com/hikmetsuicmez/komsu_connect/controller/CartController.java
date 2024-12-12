package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.CartItemRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController extends RestBaseController {

    private final CartService cartService;

    @PostMapping("/add")
    public ApiResponse<?> addToCart(@RequestBody CartItemRequest request) {
        CartItemResponse cartItem = cartService.addToCart(request.getProductId(), request.getUserId(), request.getQuantity());
        return ApiResponse.success(cartItem);
    }


    @GetMapping
    public ApiResponse<?> viewCart(@RequestParam Long userId) {
        List<CartItemResponse> cartItems = cartService.viewCart(userId);
        return ApiResponse.success(cartItems);
    }

}
