package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.response.MessageDTO;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageDTO sendMessage(Long receiverId, String content);
    List<MessageResponse> getConversationBetweenUsers(Long userId, Long selectedUserId);
    List<MessageResponse> getMessageHistory(Long userId);
    List<MessageResponse> getInboxMessages();
    void markAsRead(Long messageId);
    MessageResponse getOrCreateConversation(Long userId, Long selectedUserId);

}
