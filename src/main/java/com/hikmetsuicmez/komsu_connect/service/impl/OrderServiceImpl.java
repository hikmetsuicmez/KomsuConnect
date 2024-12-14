package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.*;
import com.hikmetsuicmez.komsu_connect.mapper.OrderItemMapper;
import com.hikmetsuicmez.komsu_connect.mapper.OrderMapper;
import com.hikmetsuicmez.komsu_connect.repository.*;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import com.hikmetsuicmez.komsu_connect.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

        private final OrderRepository orderRepository;
        private final CartService cartService;
        private final CartItemRepository cartItemRepository;
        private final OrderItemRepository orderItemRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Override
        @Transactional
        public OrderResponse createOrder(Long userId) {
                log.info("Creating order - User ID: {}", userId);

                List<CartItemResponse> cartItems = cartService.viewCart(userId);

                if (cartItems.isEmpty()) {
                        log.warn("Cart is empty for User ID: {}", userId);
                        throw new RuntimeException("Cart is empty");
                }

                double totalPrice = cartItems.stream().mapToDouble(item -> {
                        Product product = productRepository.findById(item.getProductId())
                                    .orElseThrow(() -> new RuntimeException("Product not found"));
                        return product.getPrice() * item.getQuantity();
                }).sum();

                User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                Order order = new Order();
                order.setUser(user);
                order.setTotalPrice(totalPrice);
                order.setCreatedAt(LocalDateTime.now());

                Order savedOrder = orderRepository.save(order);
                log.info("Order created - Order ID: {}", savedOrder.getId());

                for (CartItemResponse cartItemResponseDTO : cartItems) {
                        Product product = productRepository.findById(cartItemResponseDTO.getProductId())
                                    .orElseThrow(() -> new RuntimeException("Product not found"));

                        OrderItem orderItem = OrderItemMapper.toEntity(new OrderItemResponse(cartItemResponseDTO.getProductId(),
                                    cartItemResponseDTO.getProductName(), cartItemResponseDTO.getQuantity(), product.getPrice()), savedOrder, product);

                        orderItemRepository.save(orderItem);

                        CartItem cartItem = cartItemRepository.findByProductIdAndUserId(cartItemResponseDTO.getProductId(), userId).orElse(null);

                        if (cartItem != null) {
                                log.info("Deleting CartItem - CartItem ID: {}", cartItem.getId());
                                cartItemRepository.delete(cartItem);
                        } else {
                                log.warn("CartItem not found - Product ID: {}, User ID: {}", cartItemResponseDTO.getProductId(), userId);
                        }

                }
                return OrderMapper.toResponseDto(savedOrder);
        }

        @Override
        public List<OrderResponse> getOrderHistory(Long userId) {
                log.info("Getting order history - User ID: {}", userId);

                List<Order> orders = orderRepository.findByUserId(userId);
                return orders
                            .stream()
                            .map(OrderMapper::toResponseDto)
                            .sorted(Comparator.comparing(OrderResponse::getCreatedAt).reversed())
                            .toList();
        }

}