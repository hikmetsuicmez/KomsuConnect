package com.hikmetsuicmez.komsu_connect.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> orderItems;
}
