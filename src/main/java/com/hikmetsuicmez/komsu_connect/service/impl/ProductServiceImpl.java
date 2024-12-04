package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void rateProduct(Long productId, Double rating) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new UserNotFoundException("Product not found"));

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        product.setRating(rating);
        productRepository.save(product);
    }
}
