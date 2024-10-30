package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.Notification;
import com.hikmetsuicmez.komsu_connect.entity.User;

public interface NotificationService {

    Notification createNotification(User user, String message);
    void sendEmailNotification(User user, String subject, String content);
    void sendNewMessageNotification(User sender, User receiver, String messageContent);
}
