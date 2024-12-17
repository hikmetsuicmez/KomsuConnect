package com.hikmetsuicmez.komsu_connect.service;


import com.hikmetsuicmez.komsu_connect.response.ProductResponse;

public interface ProductService {
    void rateProduct(Long productId, Double rating);

    ProductResponse getLatestProduct();
}
