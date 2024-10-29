package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Long receiverId, String content);
    List<Message> getMessageHistory(Long userId);
}
