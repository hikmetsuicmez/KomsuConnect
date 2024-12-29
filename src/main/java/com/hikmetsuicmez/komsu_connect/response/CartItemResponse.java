package com.hikmetsuicmez.komsu_connect.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {

    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price; // Ürünün fiyatı
    private String photoUrl; // Ürün fotoğraf URL'si
    private String businessName; // İşletme adı
    
}
