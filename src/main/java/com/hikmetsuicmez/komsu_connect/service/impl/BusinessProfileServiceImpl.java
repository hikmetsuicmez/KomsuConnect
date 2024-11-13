package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessProfileServiceImpl implements BusinessProfileService {

    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public void addProduct(ProductRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_BUSINESS_OWNER) {
            throw new RuntimeException("Only business owners can add products.");
        }

        BusinessProfile businessProfile = currentUser.getBusinessProfile();

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setBusinessProfile(businessProfile);

        productRepository.save(product);
    }
}
