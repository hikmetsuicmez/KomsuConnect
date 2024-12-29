package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;
    private LocalDateTime createdAt;
}
