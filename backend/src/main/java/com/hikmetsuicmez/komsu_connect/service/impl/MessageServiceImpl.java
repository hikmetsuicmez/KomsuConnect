package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.MessageMapper;
import com.hikmetsuicmez.komsu_connect.repository.MessageRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.response.MessageDTO;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.MessageService;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

        Message savedMessage = messageRepository.save(message);

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

        notificationService.sendNewMessageNotification(sender, receiver, content);

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

    public MessageResponse getOrCreateConversation(Long userId, Long selectedUserId) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(userId, selectedUserId);

        if (!messages.isEmpty()) {
            Message latestMessage = messages.get(messages.size() - 1);
            return MessageResponse.builder()
                    .id(latestMessage.getId())
                    .sender(new UserSummary(latestMessage.getSender().getId(), latestMessage.getSender().getFirstName()))
                    .receiver(new UserSummary(latestMessage.getReceiver().getId(), latestMessage.getReceiver().getFirstName()))
                    .content(latestMessage.getContent())
                    .timestamp(latestMessage.getTimestamp())
                    .businessName(latestMessage.getReceiver().getBusinessProfile().getBusinessName() != null
                            ? latestMessage.getReceiver().getBusinessProfile().getBusinessName()
                            : latestMessage.getSender().getBusinessProfile().getBusinessName())
                    .build();

        }

        User sender = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = userRepository.findById(selectedUserId).orElseThrow(() -> new RuntimeException("User not found"));

        return MessageResponse.builder()
                .id(null)
                .sender(new UserSummary(sender.getId(), sender.getFirstName()))
                .receiver(new UserSummary(receiver.getId(), receiver.getFirstName()))
                .content("No messages yet")
                .timestamp(null)
                .build();
    }


}
