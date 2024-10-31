package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Notification;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.repository.NotificationRepository;
import com.hikmetsuicmez.komsu_connect.service.EmailService;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String notificationMessage = sender.getFirstName() + " " + sender.getLastName() + " - Tarafından yeni bir mesaj aldınız: " + messageContent;

        createNotification(receiver, notificationMessage);

        String emailSubject = "Yeni Mesaj Aldınız";
        sendEmailNotification(receiver, emailSubject, notificationMessage);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserNotFoundException("Notification not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the recipient can mark this notification as read");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
