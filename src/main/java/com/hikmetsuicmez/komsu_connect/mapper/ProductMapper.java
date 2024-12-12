package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .photoUrl(product.getPhotoUrl())
                .build();
    }
}
