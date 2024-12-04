package com.hikmetsuicmez.komsu_connect.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;
}
