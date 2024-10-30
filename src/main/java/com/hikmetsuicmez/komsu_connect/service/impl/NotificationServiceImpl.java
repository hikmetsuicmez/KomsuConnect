package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Notification;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.repository.NotificationRepository;
import com.hikmetsuicmez.komsu_connect.service.EmailService;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;


    @Override
    public Notification createNotification(User user, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public void sendEmailNotification(User user, String subject, String content) {
        emailService.sendEmail(user.getEmail(), subject, content);
    }

    @Override
    public void sendNewMessageNotification(User sender, User receiver, String messageContent) {
        String notificationMessage = "Yeni bir mesaj aldınız: " + messageContent;

        createNotification(receiver, notificationMessage);

        String emailSubject = "Yeni Mesaj Aldınız";
        sendEmailNotification(receiver, emailSubject, notificationMessage);
    }
}
