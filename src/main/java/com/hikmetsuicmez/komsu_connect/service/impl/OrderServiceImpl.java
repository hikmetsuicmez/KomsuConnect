package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.*;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.OrderItemMapper;
import com.hikmetsuicmez.komsu_connect.mapper.OrderMapper;
import com.hikmetsuicmez.komsu_connect.repository.*;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import com.hikmetsuicmez.komsu_connect.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(Long userId) {
        List<CartItemResponse> cartItems = cartService.viewCart(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No cart items");
        }

        double totalPrice = cartItems.stream().mapToDouble(item -> {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            return product.getPrice() * item.getQuantity();
        }).sum();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        for (CartItemResponse cartItemResponse : cartItems) {
            Product product = productRepository.findById(cartItemResponse.getProductId())
                    .orElseThrow(() -> new UserNotFoundException("Product not found"));
            OrderItem orderItem = OrderItemMapper.toEntity(new OrderItemResponse
                    (cartItemResponse.getProductId(), cartItemResponse.getProductName(),
                            cartItemResponse.getQuantity(), product.getPrice()), savedOrder, product);
            orderItemRepository.save(orderItem);
            CartItem cartItem = cartItemRepository.findById(cartItemResponse.getProductId())
                    .orElseThrow(() -> new UserNotFoundException("CartItem not found"));
            cartItemRepository.delete(cartItem);
        }

        return OrderMapper.toResponseDto(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders
                .stream()
                .map(OrderMapper::toResponseDto)
                .toList();
    }
}
