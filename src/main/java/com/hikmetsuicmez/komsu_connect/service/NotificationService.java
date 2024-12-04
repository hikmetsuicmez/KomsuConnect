package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.Notification;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    Notification createNotification(User user, String message);
    List<NotificationResponse> getUserNotification(User user);
    void sendEmailNotification(User user, String subject, String content);
    void sendNewMessageNotification(User sender, User receiver, String messageContent);
    void markAsRead(Long notificationId);
}
