package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
        objectMapper = new ObjectMapper();
        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(productController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    // Tests for api/products/{productId}/rate


    @Test
    void shouldRateProductSuccessfully() throws Exception {
        // Arrange
        Long validProductId = 1L;
        Double validRating = 4.5;

        doNothing().when(productService).rateProduct(validProductId, validRating);

        // Act & Assert
        mockMvc.perform(post("/api/products/{productId}/rate", validProductId)
                        .param("rating", String.valueOf(validRating))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("The point was awarded successfully."));

        verify(productService, times(1)).rateProduct(validProductId, validRating);
    }

    // Tests for api/products/{productId}/rate


    @Test
    void shouldReturnNotFoundForInvalidProductId() throws Exception {
        // Arrange
        Long invalidProductId = 999L; // Sistemde olmayan bir ürün ID'si

        doThrow(new UserNotFoundException("Product not found with ID: " + invalidProductId))
                .when(productService).rateProduct(eq(invalidProductId), anyDouble());

        // Act & Assert
        mockMvc.perform(post("/api/products/{productId}/rate", invalidProductId)
                        .param("rating", "4.5") // Geçerli bir puan gönderiyoruz
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product not found with ID: " + invalidProductId),
                            "Response should indicate the product was not found.");
                });

        verify(productService, times(1)).rateProduct(eq(invalidProductId), anyDouble());
    }


}
