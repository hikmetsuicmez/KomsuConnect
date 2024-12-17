package com.hikmetsuicmez.komsu_connect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.handler.GlobalExceptionHandler;
import com.hikmetsuicmez.komsu_connect.response.NotificationResponse;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationController = new NotificationController(notificationService, userService);
        objectMapper = new ObjectMapper();
        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(notificationController)
                        .setControllerAdvice(new GlobalExceptionHandler())
                        .build();
    }


    // Test for /api/notifications/{notificationId}/mark-as-read

    @Test
    void shouldReturnNotFoundForInvalidNotificationId() throws Exception {
        Long invalidNotificationId = 999L;

        doThrow(new UserNotFoundException("Notification not found"))
                .when(notificationService).markAsRead(invalidNotificationId);

        mockMvc.perform(put("/api/notifications/{notificationId}/mark-as-read", invalidNotificationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    // Test for getUserNotifications


    @Test
    void shouldReturnUserNotificationsSuccessfully() throws Exception {
        List<NotificationResponse> mockNotifications = List.of(
                new NotificationResponse(1L, "Notification 1", LocalDateTime.now(), false),
                new NotificationResponse(2L, "Notification 2", LocalDateTime.now(), true)
        );

        when(notificationService.getUserNotification(any())).thenReturn(mockNotifications);

        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[1].id").value(2L));
    }

    // Test for getUserNotifications


    @Test
    void shouldReturnEmptyListWhenNoNotifications() throws Exception {
        when(notificationService.getUserNotification(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    // Test for getUnreadNotificationCount
    @Test
    void shouldReturnUnreadNotificationCountSuccessfully() throws Exception {
        Integer unreadCount = 5;

        when(notificationService.getUnreadNotificationCount()).thenReturn(unreadCount);

        mockMvc.perform(get("/api/notifications/unread-count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(unreadCount));
    }

}
