package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.request.OrderRequest;
import com.hikmetsuicmez.komsu_connect.request.PaymentRequest;
import com.hikmetsuicmez.komsu_connect.response.OrderResponse;
import com.hikmetsuicmez.komsu_connect.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

        @InjectMocks
        private OrderController orderController;

        @Mock
        private OrderService orderService;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                objectMapper = new ObjectMapper();
                mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        }

         // Tests for /order

        @Test
        void shouldCreateOrderSuccessfully() throws Exception {
                // Arrange
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setUserId(1L);

                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderId(1L);

                when(orderService.createOrder(anyLong())).thenReturn(orderResponse);

                // Act & Assert
                mockMvc.perform(post("/order")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(orderRequest)))
                            .andExpect(status().isOk())
                            .andExpect(result -> {
                                    String responseBody = result.getResponse().getContentAsString();
                                    assertTrue(responseBody.contains("1"), "Response should contain order ID.");
                            });
        }

        // Tests for /order

        @Test
        void shouldReturnBadRequestWhenOrderRequestIsInvalid() throws Exception {
                // Arrange
                OrderRequest invalidOrderRequest = new OrderRequest();

                // Act & Assert
                mockMvc.perform(post("/order")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(invalidOrderRequest)))
                            .andExpect(status().isBadRequest());
        }

        // Tests for /order/{orderId}/payment

        @Test
        void shouldProcessPaymentSuccessfully() throws Exception {
                // Arrange
                Long orderId = 1L;
                PaymentRequest paymentRequest = PaymentRequest.builder()
                            .price("100.00")
                            .cardHolderName("John Doe")
                            .cardNumber("1234567812345678")
                            .expireMonth("12")
                            .expireYear("2025")
                            .cvc("123")
                            .build();

                OrderResponse response = OrderResponse.builder()
                            .orderId(orderId)
                            .totalPrice(100.00)
                            .createdAt(LocalDateTime.now())
                            .orderItems(new ArrayList<>())
                            .build();

                when(orderService.processPayment(eq(orderId), any(PaymentRequest.class))).thenReturn(response);

                // Act & Assert
                mockMvc.perform(post("/order/{orderId}/payment", orderId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(paymentRequest)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data.orderId").value(orderId))
                            .andExpect(jsonPath("$.data.totalPrice").value(100.00))
                            .andExpect(jsonPath("$.data.orderItems").isArray());

                verify(orderService, times(1)).processPayment(eq(orderId), any(PaymentRequest.class));
        }

        // Tests for /order/history

        @Test
        void shouldGetOrderHistorySuccessfully() throws Exception {
                // Arrange
                Long userId = 1L;
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderId(1L);
                List<OrderResponse> orderHistory = Collections.singletonList(orderResponse);

                when(orderService.getOrderHistory(anyLong())).thenReturn(orderHistory);

                // Act & Assert
                mockMvc.perform(get("/order/history")
                                        .param("userId", String.valueOf(userId))
                                        .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk())
                            .andExpect(result -> {
                                    String responseBody = result.getResponse().getContentAsString();
                                    assertTrue(responseBody.contains("1"), "Response should contain order ID.");
                            });
        }

        // Tests for /order/history

        @Test
        void shouldReturnEmptyListWhenUserHasNoOrderHistory() throws Exception {
                // Arrange
                Long userId = 1L;
                when(orderService.getOrderHistory(userId)).thenReturn(Collections.emptyList());

                // Act & Assert
                mockMvc.perform(get("/order/history")
                                        .param("userId", String.valueOf(userId))
                                        .contentType(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk()) // Beklenen: 200 OK
                            .andExpect(jsonPath("$.data").isArray()) // `data` bir dizi olmalı
                            .andExpect(jsonPath("$.data").isEmpty()); // Dizi boş olmalı
        }

}