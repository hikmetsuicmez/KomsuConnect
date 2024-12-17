package com.hikmetsuicmez.komsu_connect.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessageResponse {
    private Long id;
    private UserSummary sender;
    private UserSummary receiver;
    private String content;
    private LocalDateTime timestamp;
    private String businessName;
}