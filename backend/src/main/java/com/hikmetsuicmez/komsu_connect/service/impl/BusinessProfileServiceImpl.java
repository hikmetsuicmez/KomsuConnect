package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.entity.Rating;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.BusinessProfileMapper;
import com.hikmetsuicmez.komsu_connect.mapper.ProductMapper;
import com.hikmetsuicmez.komsu_connect.repository.BusinessProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.ProductRepository;
import com.hikmetsuicmez.komsu_connect.repository.RatingRepository;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessDTO;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import com.hikmetsuicmez.komsu_connect.service.NotificationService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessProfileServiceImpl implements BusinessProfileService {

    private final UserService userService;
    private final ProductRepository productRepository;
    private final BusinessProfileRepository businessProfileRepository;
    private final RatingRepository ratingRepository;
    private final NotificationService notificationService;
    private final String uploadDir = "/uploads/business-profiles/";
    private final String uploadProductDir = "/uploads/products/";


    @Override
    public BusinessDTO getBusinessProfileById(Long businessId) {
        BusinessProfile businessProfile = businessProfileRepository.findById(businessId)
                .orElseThrow(UserNotFoundException::new);

        BusinessDTO businessDTO = new BusinessDTO();
        businessDTO.setId(businessProfile.getId());
        businessDTO.setBusinessName(businessProfile.getBusinessName());
        businessDTO.setBusinessDescription(businessProfile.getBusinessDescription());
        businessDTO.setPhotoUrl(businessProfile.getPhotoUrl());
        businessDTO.setNeighborhood(businessDTO.getNeighborhood());

        return businessDTO;

    }

    @Override
    public UserSummary getBusinessOwner(Long businessId) {
        Optional<BusinessProfile> businessProfile = businessProfileRepository.findById(businessId);
        if (businessProfile.isEmpty()) {
            throw new UserNotFoundException("Business profile not found.");
        }
        User owner = businessProfile.get().getUser();

        return UserSummary.builder()
                .id(owner.getId())
                .firstName(owner.getFirstName())
                .lastName(owner.getLastName())
                .build();
    }

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

        if ((neighborhood == null || neighborhood.isEmpty()) && (businessName == null || businessName.isEmpty())) {
            profiles = businessProfileRepository.findAll();
        }
        if (neighborhood != null && !neighborhood.isEmpty() && businessName != null && !businessName.isEmpty()) {
            profiles = businessProfileRepository.findByUserNeighborhoodAndBusinessName(neighborhood, businessName);
        } else if (neighborhood != null && !neighborhood.isEmpty()) {
            profiles = businessProfileRepository.findByUserNeighborhood(neighborhood);
        } else if (businessName != null && !businessName.isEmpty()) {
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
                .orElseThrow(() -> new UserNotFoundException("Product not found with ID: " + productId));

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

    @Override
    public List<BusinessDTO> getPublicBusinesses() {
        return businessProfileRepository.findAll().stream()
                .map(business -> BusinessDTO.builder()
                        .id(business.getId())
                        .businessName(business.getBusinessName())
                        .businessDescription(business.getBusinessDescription())
                        .neighborhood(business.getNeighborhood())
                        .photoUrl(business.getPhotoUrl())
                        .rating(ratingRepository.calculateAverageRatingForBusiness(business.getId()))
                        .build())
                .toList();
    }

    @Override
    public void rateBusiness(Long businessId, Double ratingValue) {
        BusinessProfile businessProfile = businessProfileRepository.findById(businessId)
                .orElseThrow(() -> new UserNotFoundException("Business profile not found with ID: " + businessId));

        User currentUser = userService.getCurrentUser();

        Optional<Rating> existingRating = ratingRepository.findByBusinessAndUser(businessProfile, currentUser);

        if (existingRating.isPresent()) {
            throw new IllegalArgumentException("You have already rated this business.");
        }

        Rating newRating = new Rating();
        newRating.setBusiness(businessProfile);
        newRating.setUser(currentUser);
        newRating.setRating(ratingValue);
        ratingRepository.save(newRating);

        // Bildirim oluştur.
        String message = "Kullanıcı " + currentUser.getUsername() + " işletmenize " + ratingValue + " puan verdi.";
        notificationService.createNotification(businessProfile.getUser(), message);

    }

    @Override
    public Double calculateBusinessAverageRating(Long businessId) {
        Double averageRating = ratingRepository.calculateAverageRatingForBusiness(businessId);
        return averageRating != null ? averageRating : 0.0;
    }

    @Override
    public String saveBusinessPhoto(Long businessId, MultipartFile file) {
        BusinessProfile businessProfile = businessProfileRepository.findById(businessId)
                .orElseThrow(() -> new UserNotFoundException("Business Not Found"));

        if (!isSupportedFileFormat(file)) {
            throw new IllegalArgumentException("Unsupported file format. Only JPEG and PNG.");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir + fileName);
        try {
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file",e);
        }

        String photoUrl = "/uploads/business-profiles/" + fileName;
        businessProfile.setPhotoUrl(photoUrl);
        businessProfileRepository.save(businessProfile);

        return photoUrl;
    }


    @Override
    public String saveProductPhoto(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        if (!isSupportedFileFormat(file)) {
            throw new IllegalArgumentException("Unsupported file format. Only JPEG and PNG.");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadProductDir + fileName);

        try {
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        String photoUrl = "/uploads/products/" + fileName;
        product.setPhotoUrl(photoUrl);
        productRepository.save(product);

        return photoUrl;
    }


    private boolean isSupportedFileFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return "image/jpeg".equals(contentType) || "image/png".equals(contentType);
    }


}
