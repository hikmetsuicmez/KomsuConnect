package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.*;
import com.hikmetsuicmez.komsu_connect.enums.OrderStatus;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.OrderItemMapper;
import com.hikmetsuicmez.komsu_connect.mapper.OrderMapper;
import com.hikmetsuicmez.komsu_connect.repository.*;
import com.hikmetsuicmez.komsu_connect.request.PaymentRequest;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderItemResponse;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import com.hikmetsuicmez.komsu_connect.service.OrderService;
import com.iyzipay.model.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        private final PaymentServiceImpl paymentService;

        @Override
        @Transactional
        public OrderResponse createOrder(Long userId) {
                log.info("Creating order for user: {}", userId);

                List<CartItemResponse> cartItems = cartService.viewCart(userId);
                if (cartItems.isEmpty()) {
                        throw new IllegalArgumentException("Cart is empty");
                }

                User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found"));

                double totalPrice = calculateTotalPrice(cartItems);

                Order order = Order.builder()
                            .user(user)
                            .totalPrice(totalPrice)
                            .status(OrderStatus.PENDING)
                            .createdAt(LocalDateTime.now())
                            .build();

                Order savedOrder = orderRepository.save(order);

                saveOrderItems(cartItems, savedOrder);
                clearCart(cartItems, userId);

                log.info("Order created successfully - Order ID: {}", savedOrder.getId());
                return OrderMapper.toResponseDto(savedOrder);
        }

        @Transactional
        public OrderResponse processPayment(Long orderId, PaymentRequest paymentRequest) {
                // Validate request
                if (paymentRequest == null ||
                            StringUtils.isEmpty(paymentRequest.getCardHolderName()) ||
                            StringUtils.isEmpty(paymentRequest.getCardNumber()) ||
                            StringUtils.isEmpty(paymentRequest.getExpireMonth()) ||
                            StringUtils.isEmpty(paymentRequest.getExpireYear()) ||
                            StringUtils.isEmpty(paymentRequest.getCvc())) {
                        throw new RuntimeException("Payment information is required");
                }

                Order order = orderRepository.findById(orderId)
                            .orElseThrow(() -> new RuntimeException("Order not found"));

                if (order.getStatus() != OrderStatus.PENDING) {
                        throw new RuntimeException("Order is not in pending status");
                }

                try {
                        // Create iyzico payment
                        Payment payment = paymentService.createPayment(
                                    String.valueOf(order.getTotalPrice()),
                                    paymentRequest.getCardHolderName(),
                                    paymentRequest.getCardNumber(),
                                    paymentRequest.getExpireMonth(),
                                    paymentRequest.getExpireYear(),
                                    paymentRequest.getCvc()
                        );

                        if (payment == null) {
                                throw new RuntimeException("Payment failed: No response from payment service");
                        }

                        if ("success".equals(payment.getStatus())) {
                                order.setStatus(OrderStatus.PAYMENT_COMPLETED);
                                order.setPaymentId(payment.getPaymentId());
                                order.setPaymentStatus(payment.getStatus());
                                order.setPaymentDate(LocalDateTime.now());
                        } else {
                                order.setStatus(OrderStatus.PAYMENT_FAILED);
                                order.setPaymentStatus(payment.getErrorMessage());
                                throw new RuntimeException("Payment failed: " + payment.getErrorMessage());
                        }

                        Order updatedOrder = orderRepository.save(order);
                        return OrderMapper.toResponseDto(updatedOrder);
                } catch (Exception e) {
                        order.setStatus(OrderStatus.PAYMENT_FAILED);
                        orderRepository.save(order);
                        throw new RuntimeException("Payment failed: " + e.getMessage());
                }
        }

        private double calculateTotalPrice(List<CartItemResponse> cartItems) {
                return cartItems.stream()
                            .mapToDouble(item -> {
                                    Product product = productRepository.findById(item.getProductId())
                                                .orElseThrow(() -> new UserNotFoundException("Product not found"));
                                    return product.getPrice() * item.getQuantity();
                            })
                            .sum();
        }

        private void saveOrderItems(List<CartItemResponse> cartItems, Order savedOrder) {
                cartItems.forEach(cartItem -> {
                        Product product = productRepository.findById(cartItem.getProductId())
                                    .orElseThrow(() -> new UserNotFoundException("Product not found"));

                        OrderItem orderItem = OrderItemMapper.toEntity(
                                    new OrderItemResponse(
                                                cartItem.getProductId(),
                                                cartItem.getProductName(),
                                                cartItem.getQuantity(),
                                                product.getPrice()
                                    ),
                                    savedOrder,
                                    product
                        );

                        orderItemRepository.save(orderItem);
                });
        }

        private void clearCart(List<CartItemResponse> cartItems, Long userId) {
                cartItems.forEach(cartItem -> {
                        cartItemRepository.findByProductIdAndUserId(cartItem.getProductId(), userId)
                                    .ifPresent(cartItemRepository::delete);
                });
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