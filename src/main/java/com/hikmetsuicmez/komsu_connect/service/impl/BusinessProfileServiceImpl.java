package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.mapper.BusinessProfileMapper;
import com.hikmetsuicmez.komsu_connect.mapper.ProductMapper;
import com.hikmetsuicmez.komsu_connect.repository.BusinessProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessProfileServiceImpl implements BusinessProfileService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;

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

    @Override
    public List<BusinessProfileResponse> searchBusinesses(String neighborhood, String businessName) {
        List<BusinessProfile> profiles;

        if (neighborhood != null && businessName != null) {
            profiles = businessProfileRepository.findByUserNeighborhoodAndBusinessName(neighborhood, businessName);
        } else if (neighborhood != null) {
            profiles = businessProfileRepository.findByUserNeighborhood(neighborhood);
        } else if (businessName != null) {
            profiles = businessProfileRepository.findByBusinessNameContainingIgnoreCase(businessName);
        } else {
            profiles = businessProfileRepository.findAll();
        }

        return BusinessProfileMapper.mapToBusinessProfileResponseList(profiles);
    }

    @Override
    public List<ProductResponse> getProductsForCurrentBusiness() {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(UserRole.ROLE_BUSINESS_OWNER)) {
            throw new AccessDeniedException("Only business owners can getProductsForCurrentBusiness.");
        }

        BusinessProfile businessProfile = currentUser.getBusinessProfile();
        if (businessProfile == null) {
            throw new IllegalStateException("Business Owner does not have an associated business profile.");
        }

        List<Product> products = productRepository.findByBusinessProfile(businessProfile);
        return products.stream().map(ProductMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByBusinessId(Long businessId) {
        BusinessProfile businessProfile = businessProfileRepository.findById(businessId)
                .orElseThrow(() -> new EntityNotFoundException("Business profile not found with ID: " + businessId));

        List<Product> products = productRepository.findByBusinessProfile(businessProfile);
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .toList();
    }

    @Override
    public void deleteProduct(Long productId) {
        User currentUser = userService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        if (!product.getBusinessProfile().getId().equals(currentUser.getBusinessProfile().getId())) {
            throw new AccessDeniedException("You do not have permission to delete this product.");
        }


        productRepository.deleteById(productId);
    }

    @Override
    public void updateProduct(ProductRequest request, Long productId) {
        User currentUser = userService.getCurrentUser();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        if (product.getBusinessProfile() == null || product.getBusinessProfile().getId() == null || currentUser.getId() == null) {
            throw new AccessDeniedException("Invalid product or user information.");
        }

        if (!product.getBusinessProfile().getId().equals(currentUser.getBusinessProfile().getId())) {
            throw new AccessDeniedException("You do not have permission to update this product.");
        }


        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        productRepository.save(product);
    }

}
