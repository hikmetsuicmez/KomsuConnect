package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageService messageService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        messageController = new MessageController(messageService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders // Burada yeniden sınıf değişkeni atanıyor
                .standaloneSetup(messageController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // Tests for api/messages/history/{userId}


    @Test
    void shouldReturnMessageHistorySuccessfully() throws Exception {
        // Arrange
        Long userId = 1L;
        List<MessageResponse> mockMessageHistory = List.of(
                MessageResponse.builder()
                        .id(1L)
                        .sender(new UserSummary(1L, "Sender User", "Sender", "USER"))
                        .receiver(new UserSummary(2L, "Receiver User", "Receiver", "USER"))
                        .content("Hello")
                        .timestamp(LocalDateTime.now())
                        .build(),
                MessageResponse.builder()
                        .id(2L)
                        .sender(new UserSummary(2L, "Receiver User", "Receiver", "USER"))
                        .receiver(new UserSummary(1L, "Sender User", "Sender", "USER"))
                        .content("Hi")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        when(messageService.getMessageHistory(userId)).thenReturn(mockMessageHistory);

        // Act & Assert
        mockMvc.perform(get("/api/messages/history/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].sender.firstName").value("Sender User"))
                .andExpect(jsonPath("$.data[0].content").value("Hello"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].receiver.firstName").value("Sender User"))
                .andExpect(jsonPath("$.data[1].content").value("Hi"));

        verify(messageService, times(1)).getMessageHistory(userId);
    }

    // Tests for api/messages/history/{userId}

    @Test
    void shouldReturnNotFoundForNonExistingUserId() throws Exception {
        // Arrange
        Long nonExistingUserId = 999L;

        when(messageService.getMessageHistory(nonExistingUserId))
                .thenThrow(new UserNotFoundException("User with ID " + nonExistingUserId + " not found"));

        // Act & Assert
        mockMvc.perform(get("/api/messages/history/{userId}", nonExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("User with ID " + nonExistingUserId + " not found"));

        verify(messageService, times(1)).getMessageHistory(nonExistingUserId);
    }

    //Tests for api/messages/inbox

    @Test
    void shouldReturnInboxMessagesSuccessfully() throws Exception {
        // Arrange
        UserSummary sender = new UserSummary(1L, "John Doe", "John", "USER");
        UserSummary receiver = new UserSummary(2L, "Jane Doe", "Jane", "USER");

        MessageResponse message1 = MessageResponse.builder()
                .id(1L)
                .sender(sender)
                .receiver(receiver)
                .content("Hello, Jane!")
                .timestamp(LocalDateTime.now())
                .build();

        MessageResponse message2 = MessageResponse.builder()
                .id(2L)
                .sender(sender)
                .receiver(receiver)
                .content("Good morning!")
                .timestamp(LocalDateTime.now())
                .build();

        List<MessageResponse> mockMessages = List.of(message1, message2);

        when(messageService.getInboxMessages()).thenReturn(mockMessages);

        // Act & Assert
        mockMvc.perform(get("/api/messages/inbox")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].content").value("Hello, Jane!"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].content").value("Good morning!"));

        verify(messageService, times(1)).getInboxMessages();
    }

    //Tests for api/messages/inbox

    @Test
    void shouldReturnEmptyListWhenInboxIsEmpty() throws Exception {
        // Arrange
        when(messageService.getInboxMessages()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/messages/inbox")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());

        verify(messageService, times(1)).getInboxMessages();
    }

    // Tests for api/messages/conversation/{userId}/{selectedUserId}

    @Test
    void shouldReturnConversationSuccessfully() throws Exception {
        // Arrange
        Long userId = 1L;
        Long selectedUserId = 2L;
        List<MessageResponse> mockConversation = List.of(
                MessageResponse.builder()
                        .id(1L)
                        .sender(new UserSummary(1L, "John Doe", "John", "USER"))
                        .receiver(new UserSummary(2L, "Jane Doe", "Jane", "USER"))
                        .content("Hello, Jane!")
                        .timestamp(LocalDateTime.now())
                        .build(),
                MessageResponse.builder()
                        .id(2L)
                        .sender(new UserSummary(2L, "Jane Doe", "Jane", "USER"))
                        .receiver(new UserSummary(1L, "John Doe", "John", "USER"))
                        .content("Good morning!")
                        .timestamp(LocalDateTime.now())
                        .build()
        );

        when(messageService.getConversationBetweenUsers(userId, selectedUserId)).thenReturn(mockConversation);

        // Act & Assert

        mockMvc.perform(get("/api/messages/conversation/{userId}/{selectedUserId}", userId, selectedUserId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].content").value("Hello, Jane!"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].content").value("Good morning!"));

        verify(messageService, times(1)).getConversationBetweenUsers(userId, selectedUserId);

    }

    // Tests for api/messages/conversation/{userId}/{selectedUserId}

    @Test
    void shouldReturnNotFoundForNonExistingUsers() throws Exception {
        // Arrange
        Long userId = 1L;
        Long nonExistingUserId = 999L;

        when(messageService.getConversationBetweenUsers(userId, nonExistingUserId))
                .thenThrow(new UserNotFoundException("User with ID " + nonExistingUserId + " not found"));

        // Act & Assert
        mockMvc.perform(get("/api/messages/conversation/{userId}/{selectedUserId}", userId, nonExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("User with ID " + nonExistingUserId + " not found"));

        verify(messageService, times(1)).getConversationBetweenUsers(userId, nonExistingUserId);
    }

}