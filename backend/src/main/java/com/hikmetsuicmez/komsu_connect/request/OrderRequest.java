package com.hikmetsuicmez.komsu_connect.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderRequest {

    private Long userId;
    private LocalDateTime createdAt;
}
