package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.response.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse sendMessage(Long receiverId, String content);
    List<MessageResponse> getMessageHistory(Long userId);
}
