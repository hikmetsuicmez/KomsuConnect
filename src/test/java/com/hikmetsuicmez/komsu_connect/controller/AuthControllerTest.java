package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.request.AuthRequest;
import com.hikmetsuicmez.komsu_connect.request.BusinessOwnerRegisterRequest;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.AuthResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthServiceImpl authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);
        objectMapper = new ObjectMapper();
        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(authController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // Arrange
        AuthRequest validRequest = new AuthRequest("validUser", "validPassword");
        UserSummary mockUserSummary = UserSummary.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .role("USER")
                .build();
        AuthResponse authResponse = new AuthResponse("validToken", "Login successful", mockUserSummary);

        when(authService.login(any(AuthRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("validToken"), "Response should contain the valid token.");
                    assertTrue(responseBody.contains("Login successful"), "Response should contain the success message.");
                    assertTrue(responseBody.contains("John"), "Response should contain user first name.");
                    assertTrue(responseBody.contains("Doe"), "Response should contain user last name.");
                    assertTrue(responseBody.contains("USER"), "Response should contain user role.");
                });
    }



    @Test
    void shouldReturnErrorForInvalidCredentials() throws Exception {
        // Arrange
        AuthRequest invalidRequest = new AuthRequest("invalidUser", "invalidPassword");

        when(authService.login(any(AuthRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Invalid username or password"),
                            "Response should indicate invalid credentials.");
                });
    }

    @Test
    void shouldReturnBadRequestForMissingFields() throws Exception {
        // Arrange
        AuthRequest invalidRequest = new AuthRequest("", ""); // Eksik kullanıcı adı ve şifre

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody); // Yanıtı kontrol için yazdır
                    assertTrue(responseBody.contains("\"result\":false"), "Response should indicate validation failure.");
                    assertTrue(responseBody.contains("\"errorMessage\":\"Validation Error\""), "Response should include validation error message.");
                    assertTrue(responseBody.contains("\"password\":\"Password must not be blank\""), "Response should indicate password is missing.");
                    assertTrue(responseBody.contains("\"email\":\"Email must not be blank\""), "Response should indicate email is missing.");
                });
    }



    @Test
    void shouldReturnErrorForRestrictedUser() throws Exception {
        // Arrange
        AuthRequest restrictedRequest = new AuthRequest("restrictedUser", "validPassword");

        when(authService.login(any(AuthRequest.class)))
                .thenThrow(new IllegalArgumentException("User account is locked"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restrictedRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("User account is locked"),
                            "Response should indicate restricted user account.");
                });
    }


    @Test
    void shouldSuccessfullyRegisterUserWithValidInput() {
        // Arrange
        RegisterRequest validRequest = new RegisterRequest();
        String successMessage = "Registration successful.";
        when(authService.register(validRequest)).thenReturn(successMessage);

        // Act
        ApiResponse<String> response = authController.register(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Registration successful.", response.getData());
        assertTrue(response.isResult());
        verify(authService, times(1)).register(validRequest);
    }


    @Test
    void shouldReturn400BadRequestForInvalidInputWithMockMvc() throws Exception {
        // Arrange
        RegisterRequest invalidRequest = new RegisterRequest();
        invalidRequest.setEmail(""); // Empty email should trigger validation error

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        // Act & Assert
        mockMvc.perform(post("/auth/register/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Validation Error"));
                });
    }


    @Test
    void shouldValidateEmailFormat() throws Exception {
        // Arrange
        RegisterRequest invalidEmailRequest = new RegisterRequest();
        invalidEmailRequest.setEmail("invalid-email"); // Invalid email format
        invalidEmailRequest.setPassword("ValidPassword123");

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Act & Assert
        mockMvc.perform(post("/auth/register/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Email should be valid"));
                });
    }

    @Test
    void shouldReturnErrorForWeakPassword() throws Exception {
        // Arrange
        RegisterRequest weakPasswordRequest = new RegisterRequest();
        weakPasswordRequest.setEmail("valid@email.com");
        weakPasswordRequest.setPassword("weak"); // Weak password

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Act & Assert
        mockMvc.perform(post("/auth/register/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weakPasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Password must be between 6 and 20 characters."));
                });
    }

    @Test
    void shouldSuccessfullyRegisterBusinessWithValidInputData() throws Exception {
        // Arrange
        BusinessOwnerRegisterRequest validRequest = new BusinessOwnerRegisterRequest();
        validRequest.setBusinessName("Valid Business");
        validRequest.setBusinessDescription("This is a valid business description.");
        validRequest.setFirstName("John");
        validRequest.setLastName("Doe");
        validRequest.setEmail("business@example.com");
        validRequest.setPassword("ValidPassword123");
        validRequest.setPhoneNumber("1234567890");
        validRequest.setRole(UserRole.ROLE_BUSINESS_OWNER);
        validRequest.setNeighborhood("Downtown");

        // Act & Assert
        mockMvc.perform(post("/auth/register/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody);
                    assertTrue(responseBody.contains("Registration successful."),
                            "Response should indicate successful registration.");
                });
    }


    @Test
    void shouldValidateUniqueBusinessNameAndReturnErrorForDuplicateNames() throws Exception {
        // Arrange
        BusinessOwnerRegisterRequest duplicateRequest = new BusinessOwnerRegisterRequest();
        duplicateRequest.setBusinessName("Duplicate Business");
        duplicateRequest.setBusinessDescription("Valid description");
        duplicateRequest.setFirstName("John");
        duplicateRequest.setLastName("Doe");
        duplicateRequest.setEmail("business@example.com");
        duplicateRequest.setPassword("ValidPassword123");
        duplicateRequest.setPhoneNumber("1234567890");
        duplicateRequest.setNeighborhood("Downtown");
        duplicateRequest.setRole(UserRole.ROLE_BUSINESS_OWNER);

        doThrow(new IllegalArgumentException("Business name already exists"))
                .when(authService).registerBusinessOwner(any(BusinessOwnerRegisterRequest.class));

        // Act & Assert
        mockMvc.perform(post("/auth/register/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody);
                    assertTrue(responseBody.contains("Business name already exists"),
                            "Response should indicate duplicate business name error.");
                });
    }


    @Test
    void shouldEnforceMaximumLengthConstraintsOnBusinessName() throws Exception {
        // Arrange
        BusinessOwnerRegisterRequest longNameRequest = new BusinessOwnerRegisterRequest();
        longNameRequest.setBusinessName("A".repeat(300)); // Fazla uzun bir işletme adı
        longNameRequest.setBusinessDescription("Valid description");
        longNameRequest.setFirstName("John");
        longNameRequest.setLastName("Doe");
        longNameRequest.setEmail("business@example.com");
        longNameRequest.setPassword("ValidPassword123");
        longNameRequest.setPhoneNumber("1234567890");
        longNameRequest.setNeighborhood("Downtown");
        longNameRequest.setRole(UserRole.ROLE_BUSINESS_OWNER);

        // Act & Assert
        mockMvc.perform(post("/auth/register/business")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longNameRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println("Response Body: " + responseBody);
                    assertTrue(responseBody.contains("Business name must be between 3 and 50 characters"),
                            "Response should indicate input length validation failure.");
                });
    }


}


