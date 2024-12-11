package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.CartItem;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.CartItemMapper;
import com.hikmetsuicmez.komsu_connect.repository.CartItemRepository;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartItemResponse addToCart(Long productId, Long userId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new UserNotFoundException("Product not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setUser(user);
        cartItem.setQuantity(quantity);

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartItemMapper.toResponseDto(savedCartItem);
    }


    @Override
    public List<CartItemResponse> viewCart(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return cartItems
                .stream()
                .map(CartItemMapper::toResponseDto)
                .toList();
    }
}
