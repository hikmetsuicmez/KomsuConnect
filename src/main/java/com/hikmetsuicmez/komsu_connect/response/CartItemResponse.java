package com.hikmetsuicmez.komsu_connect.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price; // Ürünün fiyatı
    private String photoUrl; // Ürün fotoğraf URL'si

}
