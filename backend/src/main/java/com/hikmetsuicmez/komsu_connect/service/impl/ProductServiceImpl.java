package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.repository.BusinessProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import com.hikmetsuicmez.komsu_connect.service.ProductService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private final UserService userService;

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

    @Override
    public ProductResponse getLatestProduct() {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != UserRole.ROLE_BUSINESS_OWNER) {
            throw new RuntimeException("Only business owners can view their latest product.");
        }

        BusinessProfile businessProfile = currentUser.getBusinessProfile();

        Product latestProduct = productRepository.findLatestProductByBusinessProfile(businessProfile);

        if (latestProduct == null) {
            throw new RuntimeException("No products found for the current business profile.");
        }

        return ProductResponse.builder()
                .id(latestProduct.getId())
                .name(latestProduct.getName())
                .description(latestProduct.getDescription())
                .price(latestProduct.getPrice())
                .photoUrl(latestProduct.getPhotoUrl())
                .build();
    }


}
