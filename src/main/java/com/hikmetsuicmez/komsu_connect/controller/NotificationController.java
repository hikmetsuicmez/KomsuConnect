package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.NotificationResponse;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController extends RestBaseController {

    private final NotificationService notificationService;
    private final UserService userService;

    @PutMapping("/{notificationId}/mark-as-read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getUserNotifications() {
        User currentUser = userService.getCurrentUser();
        List<NotificationResponse> notifications = notificationService.getUserNotification(currentUser);
        return ApiResponse.success(notifications);
    }
}
