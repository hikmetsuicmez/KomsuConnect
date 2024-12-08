package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.request.EmailUpdateRequest;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(userController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @Test
    void shouldRetrieveAllUsersSuccessfully() throws Exception {
        // Arrange
        UserSummary user1 = new UserSummary(1L, "John", "Doe", "USER");
        UserSummary user2 = new UserSummary(2L, "Jane", "Smith", "ADMIN");
        List<UserSummary> users = List.of(user1, user2);

        when(userService.retrieveAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    ApiResponse<List<UserSummary>> response = objectMapper.readValue(
                            responseBody,
                            new TypeReference<>() {
                            }
                    );
                    // Kontrollü loglama
                    System.out.println("Response Body: " + response.getData());
                    assertTrue(response.getData().stream().anyMatch(u -> u.getFirstName().equals("John") && u.getLastName().equals("Doe")),
                            "Response should contain user1 (John Doe).");
                    assertTrue(response.getData().stream().anyMatch(u -> u.getFirstName().equals("Jane") && u.getLastName().equals("Smith")),
                            "Response should contain user2 (Jane Smith).");
                });
    }


    @Test
    void shouldReturnEmptyListWhenNoUsersAreFound() throws Exception {
        // Arrange
        when(userService.retrieveAllUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    ApiResponse<List<UserSummary>> response = objectMapper.readValue(
                            responseBody,
                            new TypeReference<>() {
                            }
                    );
                    assertTrue(response.getData().isEmpty(), "Response should return an empty list.");
                });
    }

    @Test
    void shouldRetrieveCurrentUserSuccessfully() throws Exception {
        // Arrange
        UserSummary mockUserSummary = UserSummary.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .role("USER")
                .build();

        when(userService.getCurrentUserProfile()).thenReturn(mockUserSummary);

        // Act & Assert
        mockMvc.perform(get("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {
                    String responseBody = mvcResult.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("John"), "Response should contain user's first name.");
                    assertTrue(responseBody.contains("Doe"), "Response should contain user's last name.");
                    assertTrue(responseBody.contains("USER"), "Response should contain user's role.");
                });
    }

    @Test
    void shouldRetrieveUserProfileSuccessfully() throws Exception {
        // Arrange
        Long userId = 1L;
        UserSummary mockUserSummary = UserSummary.builder()
                .id(userId)
                .firstName("Jane")
                .lastName("Smith")
                .role("ADMIN")
                .build();

        when(userService.getUserProfile(userId)).thenReturn(mockUserSummary);

        // Act & Assert
        mockMvc.perform(get("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Jane"), "Response should contain user's first name.");
                    assertTrue(responseBody.contains("Smith"), "Response should contain user's last name.");
                    assertTrue(responseBody.contains("ADMIN"), "Response should contain user's role.");
                });
    }

    @Test
    void shouldUpdateUserProfileSuccessfully() throws Exception {
        // Arrange
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setFirstName("UpdatedFirstName");
        userProfileRequest.setLastName("UpdatedLastName");
        userProfileRequest.setPhoneNumber("9876543210");

        BusinessProfileResponse mockResponse = new BusinessProfileResponse();
        mockResponse.setFirstName("UpdatedFirstName");
        mockResponse.setLastName("UpdatedLastName");
        mockResponse.setPhoneNumber("9876543210");

        when(userService.updateUserProfile(any(UserProfileRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(put("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userProfileRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("UpdatedFirstName"), "Response should contain updated first name.");
                    assertTrue(responseBody.contains("UpdatedLastName"), "Response should contain updated last name.");
                    assertTrue(responseBody.contains("9876543210"), "Response should contain updated phone number.");
                });
    }

    @Test
    void shouldUpdateEmailSuccessfully() throws Exception {
        // Arrange
        EmailUpdateRequest emailUpdateRequest = new EmailUpdateRequest();
        emailUpdateRequest.setNewEmail("newemail@example.com");

        String successMessage = "Email updated successfully. Please login again.";

        doNothing().when(userService).updateEmail(anyString());

        // Act & Assert
        mockMvc.perform(put("/api/users/update-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains(successMessage), "Response should contain the success message.");
                });
    }


}
