package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;

public class MessageMapper {

    public static MessageResponse toMessageResponse(Message message) {
        MessageResponse messageResponse = MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();

        UserSummary senderSummary = UserSummary.builder()
                .id(message.getSender().getId())
                .firstName(message.getSender().getFirstName())
                .lastName(message.getSender().getLastName())
                .build();
        messageResponse.setSender(senderSummary);

        UserSummary receiverSummary = UserSummary.builder()
                .id(message.getReceiver().getId())
                .firstName(message.getReceiver().getFirstName())
                .lastName(message.getReceiver().getLastName())
                .build();
        messageResponse.setReceiver(receiverSummary);

        return messageResponse;
    }
}
