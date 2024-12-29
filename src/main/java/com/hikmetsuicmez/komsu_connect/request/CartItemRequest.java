package com.hikmetsuicmez.komsu_connect.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {

    private Long productId;
    private Long userId;
    private Integer quantity;
}

