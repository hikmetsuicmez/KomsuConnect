package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.MessageMapper;
import com.hikmetsuicmez.komsu_connect.repository.MessageRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.response.MessageDTO;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.service.MessageService;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate brokerMessagingTemplate;

    @Override
    public MessageDTO sendMessage(Long receiverId, String content) {
        User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + receiverId + " not found"));

        // Mesaj oluştur
        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        // Mesajı kaydet
        Message savedMessage = messageRepository.save(message);

        // WebSocket üzerinden gönder
        brokerMessagingTemplate.convertAndSend(
                "/topic/messages/" + receiverId,
                MessageDTO.builder()
                        .id(savedMessage.getId())
                        .senderName(sender.getFirstName())
                        .receiverName(receiver.getFirstName())
                        .content(content)
                        .timestamp(savedMessage.getTimestamp())
                        .build()
        );

        // Notification (opsiyonel)
        notificationService.sendNewMessageNotification(sender, receiver, content);

        // DTO'ya çevir ve dön
        return MessageDTO.builder()
                .id(savedMessage.getId())
                .senderName(sender.getFirstName())
                .receiverName(receiver.getFirstName())
                .content(savedMessage.getContent())
                .timestamp(savedMessage.getTimestamp())
                .build();
    }


    @Override
    public List<MessageResponse> getConversationBetweenUsers(Long userId, Long selectedUserId) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(userId, selectedUserId);
        return messages
                .stream()
                .map(MessageMapper::toMessageResponse)
                .toList();
    }

    @Override
    public List<MessageResponse> getMessageHistory(Long userId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User otherUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        List<Message> lists = messageRepository.findBySenderAndReceiverOrReceiverAndSender(currentUser, otherUser, otherUser, currentUser);
        return lists
                .stream()
                .map(MessageMapper::toMessageResponse)
                .toList();
    }

    @Override
    public List<MessageResponse> getInboxMessages() {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Message> messages = messageRepository.findByReceiver(currentUser);
        return messages.stream()
                .map(MessageMapper::toMessageResponse)
                .toList();
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new UserNotFoundException("Message with ID " + messageId + " not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!message.getReceiver().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the recipient can mark this message as read");
        }

        message.setRead(true);
        message.setReadTimestamp(LocalDateTime.now());
        messageRepository.save(message);

        brokerMessagingTemplate
                .convertAndSend("/topic/messages/" + message.getSender().getId(), "Mesaj okundu: " + messageId);
    }



}
