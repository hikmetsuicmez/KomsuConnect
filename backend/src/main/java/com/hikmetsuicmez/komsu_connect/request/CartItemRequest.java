package com.hikmetsuicmez.komsu_connect.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemRequest {

    private Long productId;
    private Long userId;
    private Integer quantity;
}

