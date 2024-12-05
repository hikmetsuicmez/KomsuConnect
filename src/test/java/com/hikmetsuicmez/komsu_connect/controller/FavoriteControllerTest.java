package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.FavoriteResponse;
import com.hikmetsuicmez.komsu_connect.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    @InjectMocks
    private FavoriteController favoriteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnListOfFavoriteResponsesWhenUserHasMultipleFavorites() {
        // Arrange
        User mockUser = new User(); // Assuming a default constructor for User
        FavoriteResponse favorite1 = new FavoriteResponse(); // Assuming a default constructor for FavoriteResponse
        FavoriteResponse favorite2 = new FavoriteResponse(); // Assuming a default constructor for FavoriteResponse
        List<FavoriteResponse> mockFavorites = List.of(favorite1, favorite2);
        when(favoriteService.getFavoritesByUser(mockUser)).thenReturn(mockFavorites);

        // Act
        ApiResponse<List<FavoriteResponse>> response = favoriteController.getFavorites(mockUser);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getData().size());
        assertTrue(response.getData().contains(favorite1));
        assertTrue(response.getData().contains(favorite2));
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoFavorites() {
        // Arrange
        User mockUser = new User(); // Assuming a default constructor for User
        when(favoriteService.getFavoritesByUser(mockUser)).thenReturn(Collections.emptyList());

        // Act
        ApiResponse<List<FavoriteResponse>> response = favoriteController.getFavorites(mockUser);

        // Assert
        assertNotNull(response);
        assertTrue(response.getData().isEmpty());
    }

    @Test
    public void testAddFavoriteToBusiness() {
        // Arrange
        User user = new User();
        Long businessId = 1L;
        doNothing().when(favoriteService).addFavoriteForBusiness(user, businessId);

        // Act
        ApiResponse<String> response = favoriteController.addFavoriteToBusiness(user, businessId);

        // Assert
        verify(favoriteService).addFavoriteForBusiness(user, businessId);
        assertEquals("Favori işletme eklendi.", response.getData());
    }

    @Test
    void addFavoriteToBusiness_shouldThrowException_whenBusinessDoesNotExist() {
        User user = new User();
        Long nonExistentBusinessId = 999L;

        doThrow(new UserNotFoundException("Business not found"))
                .when(favoriteService).addFavoriteForBusiness(user, nonExistentBusinessId);

        assertThrows(UserNotFoundException.class, () -> {
            favoriteController.addFavoriteToBusiness(user, nonExistentBusinessId);
        });

        verify(favoriteService, times(1)).addFavoriteForBusiness(user, nonExistentBusinessId);
    }

    @Test
    public void testAddFavoriteToProduct_Success() {
        // Arrange
        User mockUser = new User();
        Long productId = 1L;
        String expectedMessage = "Favori ürün eklendi.";

        // Act
        ApiResponse<String> response = favoriteController.addFavoriteToProduct(mockUser, productId);

        // Assert
        verify(favoriteService).addFavoriteForProduct(mockUser, productId);
        assertEquals(expectedMessage, response.getData());
        assertTrue(response.isResult());
    }

    @Test
    void shouldSuccessfullyRemoveFavoriteBusinessWhenUserIsAuthenticatedAndBusinessIdIsValid() {
        // Arrange
        User mockUser = new User(); // Assuming a default constructor is available
        Long businessId = 1L;

        // Act
        ApiResponse<String> response = favoriteController.removeFavoriteFromBusiness(mockUser, businessId);

        // Assert
        assertNotNull(response);
        assertEquals("Favori işletme kaldırıldı.", response.getData());
        verify(favoriteService).removeFavoriteForBusiness(mockUser, businessId);
    }

    @Test
    void shouldReturnErrorWhenBusinessIdIsNegative() {
        User user = new User(); // Assuming User object can be instantiated like this
        Long negativeBusinessId = -1L;

        doThrow(new IllegalArgumentException("Invalid business ID"))
                .when(favoriteService).removeFavoriteForBusiness(user, negativeBusinessId);

        assertThrows(IllegalArgumentException.class, () -> {
            favoriteController.removeFavoriteFromBusiness(user, negativeBusinessId);
        });
    }

    @Test
    public void shouldRemoveFavoriteProductSuccessfully() {
        // Arrange
        User mockUser = new User();
        Long productId = 1L;
        doNothing().when(favoriteService).removeFavoriteForProduct(mockUser, productId);

        // Act
        ApiResponse<String> response = favoriteController.removeFavoriteFromProduct(mockUser, productId);

        // Assert
        assertNotNull(response);
        assertEquals("Favori ürün kaldırıldı.", response.getData());
        verify(favoriteService, times(1)).removeFavoriteForProduct(mockUser, productId);
    }

    @Test
    public void shouldReturnErrorWhenProductIdIsNegative() {
        // Arrange
        User mockUser = new User();
        Long negativeProductId = -1L;
        doThrow(new IllegalArgumentException("Product ID cannot be negative"))
                .when(favoriteService).removeFavoriteForProduct(mockUser, negativeProductId);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            favoriteController.removeFavoriteFromProduct(mockUser, negativeProductId);
        });

        assertEquals("Product ID cannot be negative", exception.getMessage());
        verify(favoriteService, times(1)).removeFavoriteForProduct(mockUser, negativeProductId);
    }


}
