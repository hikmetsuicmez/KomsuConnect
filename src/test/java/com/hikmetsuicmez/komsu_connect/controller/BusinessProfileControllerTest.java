package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessDTO;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Collections;
import java.util.List;

class BusinessProfileControllerTest {

    @InjectMocks
    private BusinessProfileController businessProfileController;

    @Mock
    private BusinessProfileService businessProfileService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        businessProfileController = new BusinessProfileController(businessProfileService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(businessProfileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    // Tests for /api/business/search

    @Test
    void shouldSearchBusinessesSuccessfully() throws Exception {
        // Arrange
        List<BusinessProfileResponse> mockBusinesses = List.of(
                BusinessProfileResponse.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .phoneNumber("123456789")
                        .neighborhood("Neighborhood 1")
                        .businessName("Business 1")
                        .businessDescription("Description 1")
                        .products(List.of()) // Boş bir ürün listesi
                        .build(),
                BusinessProfileResponse.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .phoneNumber("987654321")
                        .neighborhood("Neighborhood 2")
                        .businessName("Business 2")
                        .businessDescription("Description 2")
                        .products(List.of()) // Boş bir ürün listesi
                        .build()
        );

        when(businessProfileService.searchBusinesses("Neighborhood 1", "Business 1"))
                .thenReturn(mockBusinesses);

        // Act & Assert
        mockMvc.perform(get("/api/business/search")
                        .param("neighborhood", "Neighborhood 1")
                        .param("businessName", "Business 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody); // Debug için
                    assertTrue(responseBody.contains("Business 1"), "Response should contain 'Business 1'");
                    assertTrue(responseBody.contains("Neighborhood 1"), "Response should contain 'Neighborhood 1'");
                });
    }

    // Tests for /api/business/search

    @Test
    void shouldReturnEmptyListWhenNoParamsProvided() throws Exception {
        // Arrange
        when(businessProfileService.searchBusinesses(null, null))
                .thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/business/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("[]"), "Response should return an empty list");
                });
    }

    // Tests for /api/business/{businessId}

    @Test
    void shouldRetrieveBusinessByIdSuccessfully() throws Exception {
        // Arrange
        Long businessId = 1L;
        BusinessDTO mockBusiness = BusinessDTO.builder()
                .id(businessId)
                .businessName("Test Business")
                .businessDescription("A test business description")
                .neighborhood("Downtown")
                .rating(1.5)
                .build();

        when(businessProfileService.getBusinessProfileById(businessId)).thenReturn(mockBusiness);

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}", businessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Test Business"), "Response should contain the business name.");
                    assertTrue(responseBody.contains("A test business description"), "Response should contain the business description.");
                    assertTrue(responseBody.contains("Downtown"), "Response should contain the neighborhood.");
                });
    }


    // Tests for /api/business/{businessId}

    @Test
    void shouldReturnErrorForNonExistingBusinessId() throws Exception {
        // Arrange
        Long nonExistingBusinessId = 999L;

        when(businessProfileService.getBusinessProfileById(nonExistingBusinessId))
                .thenThrow(new UserNotFoundException("Business not found"));

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}", nonExistingBusinessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Fix the status code expectation to 404 Not Found
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Business not found"), "Response should indicate that the business was not found.");
                });
    }

    // Test for /api/business/add-product

    @Test
    void shouldAddProductSuccessfully() throws Exception {
        // Arrange
        ProductRequest validProductRequest = new ProductRequest();
        validProductRequest.setName("Product 1");
        validProductRequest.setDescription("Description for Product 1");
        validProductRequest.setPrice(100.0);

        // Mock Service Response
        doNothing().when(businessProfileService).addProduct(any(ProductRequest.class));

        // Act & Assert
        mockMvc.perform(post("/api/business/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product added successfully"),
                            "Response should indicate product was added successfully.");
                });
    }

    // Test for /api/business/add-product

    @Test
    void shouldReturnValidationErrorForMissingProductFields() throws Exception {
        // Arrange
        ProductRequest invalidProductRequest = new ProductRequest();

        // Act & Assert
        mockMvc.perform(post("/api/business/add-product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"errorMessage\":\"Validation Error\""),
                            "Response should include validation error message.");
                    assertTrue(responseBody.contains("name"),
                            "Response should indicate that 'name' field is missing.");
                    assertTrue(responseBody.contains("description"),
                            "Response should indicate that 'description' field is missing.");
                    assertTrue(responseBody.contains("price"),
                            "Response should indicate that 'price' field is missing.");
                });
    }

    // Tests for /api/business/products

    @Test
    void shouldRetrieveAllProductsSuccessfully() throws Exception {
        // Arrange
        ProductResponse product1 = new ProductResponse(1L, "Product 1", "Description 1", 9.99);
        ProductResponse product2 = new ProductResponse(2L, "Product 2", "Description 2", 19.99);
        List<ProductResponse> mockProducts = List.of(product1, product2);

        when(businessProfileService.getProductsForCurrentBusiness()).thenReturn(mockProducts);

        // Act & Assert
        mockMvc.perform(get("/api/business/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product 1"), "Response should contain 'Product 1'");
                    assertTrue(responseBody.contains("Product 2"), "Response should contain 'Product 2'");
                });
    }

    // Tests for /api/business/products

    @Test
    void shouldReturnErrorWhenRetrievingProductsFails() throws Exception {
        // Arrange
        doThrow(new IllegalStateException("Unable to retrieve products"))
                .when(businessProfileService).getProductsForCurrentBusiness();

        // Act & Assert
        mockMvc.perform(get("/api/business/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Unable to retrieve products"), "Response should indicate an error");
                });
    }

    // Tests for /api/business/public-businesses

    @Test
    void shouldReturnListOfPublicBusinessesSuccessfully() throws Exception {
        // Arrange
        BusinessDTO business1 = new BusinessDTO(1L, "Business 1", "Description 1", 4.2, "Neighborhood 1");
        BusinessDTO business2 = new BusinessDTO(2L, "Business 2", "Description 2", 4.7, "Neighborhood 2");
        List<BusinessDTO> mockBusinesses = List.of(business1, business2);

        when(businessProfileService.getPublicBusinesses()).thenReturn(mockBusinesses);

        // Act & Assert
        mockMvc.perform(get("/api/business/public-businesses"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Business 1"), "Response should contain 'Business 1'");
                    assertTrue(responseBody.contains("Neighborhood 1"), "Response should contain 'Neighborhood 1'");
                });
    }

    // Tests for /api/business/public-businesses

    @Test
    void shouldReturnEmptyListWhenNoPublicBusinessesExist() throws Exception {
        // Arrange
        when(businessProfileService.getPublicBusinesses()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/business/public-businesses"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("[]"), "Response should indicate an empty list.");
                });
    }

    // Tests for /api/business/public-businesses


    @Test
    void shouldReturnErrorWhenFetchingPublicBusinessesFails() throws Exception {
        // Arrange
        when(businessProfileService.getPublicBusinesses())
                .thenThrow(new IllegalStateException("Failed to fetch public businesses"));

        // Act & Assert
        mockMvc.perform(get("/api/business/public-businesses"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Failed to fetch public businesses"),
                            "Response should indicate an error.");
                });
    }

    // Tests for /api/business/rate

    @Test
    void shouldRateBusinessSuccessfully() throws Exception {
        // Arrange
        Long validBusinessId = 1L;
        Double validRating = 4.5;

        doNothing().when(businessProfileService).rateBusiness(validBusinessId, validRating);

        // Act & Assert
        mockMvc.perform(post("/api/business/rate")
                        .param("businessId", String.valueOf(validBusinessId))
                        .param("rating", String.valueOf(validRating))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Rate successfully"), "Response should indicate success.");
                });

        verify(businessProfileService, times(1)).rateBusiness(validBusinessId, validRating);
    }

    // Tests for /api/business/rate


    @Test
    void shouldReturnErrorWhenRatingBusinessTwice() throws Exception {
        // Arrange
        Long validBusinessId = 1L;
        Double validRating = 4.0;


        doThrow(new IllegalArgumentException("You have already rated this business."))
                .when(businessProfileService).rateBusiness(validBusinessId, validRating);

        // Act & Assert
        mockMvc.perform(post("/api/business/rate")
                        .param("businessId", String.valueOf(validBusinessId))
                        .param("rating", String.valueOf(validRating))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 400 bekleniyor
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("You have already rated this business."),
                            "Response should indicate that the business was already rated.");
                });

        verify(businessProfileService).rateBusiness(validBusinessId, validRating);
    }


    // Tests for /api/business/{businessId}/average-rating

    @Test
    void shouldReturnAverageRatingSuccessfully() throws Exception {
        // Arrange
        Long businessId = 1L;
        Double mockAverageRating = 4.2;

        when(businessProfileService.calculateBusinessAverageRating(businessId)).thenReturn(mockAverageRating);

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}/average-rating", businessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("\"data\":4.2"), "Response should contain the correct average rating");
                });

        verify(businessProfileService, times(1)).calculateBusinessAverageRating(businessId);
    }

    // Tests for /api/business/{businessId}/average-rating

    @Test
    void shouldReturnNotFoundForNonExistingBusiness() throws Exception {
        // Arrange
        Long nonExistingBusinessId = 999L;

        when(businessProfileService.calculateBusinessAverageRating(nonExistingBusinessId))
                .thenThrow(new UserNotFoundException("Business not found with ID: " + nonExistingBusinessId));

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}/average-rating", nonExistingBusinessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Business not found with ID: 999"),
                            "Response should contain appropriate error message");
                });

        verify(businessProfileService, times(1)).calculateBusinessAverageRating(nonExistingBusinessId);
    }

    // Tests for /api/business/{businessId}/average-rating

    @Test
    void shouldReturnZeroOrNullWhenNoRatingsForBusiness() throws Exception {
        // Arrange
        Long validBusinessId = 1L;
        when(businessProfileService.calculateBusinessAverageRating(validBusinessId)).thenReturn(0.0);

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}/average-rating", validBusinessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("0.0") || responseBody.contains("null"),
                            "Response should indicate no ratings available.");
                });

        verify(businessProfileService).calculateBusinessAverageRating(validBusinessId);
    }

    // Tests for /api/business/{businessId}/products

    @Test
    void shouldReturnProductsForValidBusinessId() throws Exception {
        // Arrange
        Long validBusinessId = 1L;
        List<ProductResponse> mockProducts = List.of(
                new ProductResponse(1L, "Product 1", "Description 1", 10.99),
                new ProductResponse(2L, "Product 2", "Description 2", 15.49)
        );

        when(businessProfileService.getProductsByBusinessId(validBusinessId)).thenReturn(mockProducts);

        // Act & Assert
        mockMvc.perform(get("/api/business/{businessId}/products", validBusinessId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Product 1"))
                .andExpect(jsonPath("$.data[0].price").value(10.99))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("Product 2"))
                .andExpect(jsonPath("$.data[1].price").value(15.49));

        verify(businessProfileService, times(1)).getProductsByBusinessId(validBusinessId);
    }

    // Tests for /api/business/products/{productId}

    @Test
    void shouldUpdateProductSuccessfully() throws Exception {
        // Arrange
        Long validProductId = 1L;
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product Name");
        productRequest.setDescription("Updated Product Description");
        productRequest.setPrice(99.99);

        doNothing().when(businessProfileService).updateProduct(any(ProductRequest.class), eq(validProductId));

        // Act & Assert
        mockMvc.perform(put("/api/business/products/{productId}", validProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                        .with(csrf())
                        .with(user("businessOwner").roles("BUSINESS_OWNER"))) // gerekli rol ile
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Product updated successfully"));

        verify(businessProfileService, times(1)).updateProduct(any(ProductRequest.class), eq(validProductId));
    }

    // Tests for /api/business/products/{productId} UPDATE-MAPPING

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setDescription("Updated Description");
        productRequest.setPrice(15.99);

        doThrow(new UserNotFoundException("Product not found with ID: 999"))
                .when(businessProfileService).updateProduct(any(ProductRequest.class), eq(999L));


        // Act & Assert
        mockMvc.perform(put("/api/business/products/{productId}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest))
                        .with(csrf()))
                .andExpect(status().isNotFound()) // 404 bekleniyor
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product not found"),
                            "Response should indicate the product was not found");
                });

        verify(businessProfileService, times(1)).updateProduct(any(ProductRequest.class), eq(999L));

    }

    // Tests for /api/business/products/{productId}  DELETE-MAPPING

    @Test
    void shouldDeleteProductSuccessfully() throws Exception {
        // Arrange
        Long existingProductId = 1L; // Sistemde mevcut bir ürün ID'si
        doNothing().when(businessProfileService).deleteProduct(existingProductId);

        // Act & Assert
        mockMvc.perform(delete("/api/business/products/{productId}", existingProductId)
                        .with(csrf()) // CSRF korumasını etkinleştir
                        .with(user("businessOwner").roles("BUSINESS_OWNER"))) // Kullanıcı rolünü ayarla
                .andExpect(status().isOk()) // 200 OK bekleniyor
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Product deleted successfully"), "Başarı mesajı doğrulanmalı");
                });

        verify(businessProfileService, times(1)).deleteProduct(existingProductId);
    }


}
