package com.hikmetsuicmez.komsu_connect.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class MessageDTO {

    private Long id;
    private String senderName;
    private String receiverName;
    private String content;
    private LocalDateTime timestamp;
}
