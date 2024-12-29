package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.request.CartItemRequest;
import com.hikmetsuicmez.komsu_connect.response.CartItemResponse;
import com.hikmetsuicmez.komsu_connect.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest {

        @InjectMocks
        private CartController cartController;

        @Mock
        private CartService cartService;

        private MockMvc mockMvc;
        private ObjectMapper objectMapper;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                objectMapper = new ObjectMapper();
                mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        }

        @Test
        void shouldAddItemToCartSuccessfully() throws Exception {
                CartItemRequest request = new CartItemRequest(1L, 1L, 2);
                CartItemResponse response = new CartItemResponse(1L, "A", 2,100.0, "a","b");

                when(cartService.addToCart(anyLong(), anyLong(), anyInt())).thenReturn(response);

                mockMvc.perform(post("/cart/add")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data.productId").value(1L))
                            .andExpect(jsonPath("$.data.productName").value("A"))
                            .andExpect(jsonPath("$.data.quantity").value(2))
                            .andExpect(jsonPath("$.data.price").value(100.0))
                            .andExpect(jsonPath("$.data.photoUrl").value("a"))
                            .andExpect(jsonPath("$.data.businessName").value("b"));
        }

        @Test
        void shouldViewCartItemsForUser() throws Exception {
                CartItemResponse response = CartItemResponse.builder()
                            .productId(1L)
                            .productName("A")
                            .quantity(2)
                            .price(100.0)
                            .photoUrl("a")
                            .businessName("b")
                            .build();

                when(cartService.viewCart(anyLong())).thenReturn(Collections.singletonList(response));

                mockMvc.perform(get("/cart")
                                        .param("userId", "1"))
                            .andExpect(status().isOk())
                            .andExpect(jsonPath("$.data[0].productId").value(1L))
                            .andExpect(jsonPath("$.data[0].productName").value("A"))
                            .andExpect(jsonPath("$.data[0].quantity").value(2))
                            .andExpect(jsonPath("$.data[0].price").value(100.0))
                            .andExpect(jsonPath("$.data[0].photoUrl").value("a"))
                            .andExpect(jsonPath("$.data[0].businessName").value("b"));
        }

}