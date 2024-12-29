package com.hikmetsuicmez.komsu_connect.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Long userId;
    private LocalDateTime createdAt;
}
