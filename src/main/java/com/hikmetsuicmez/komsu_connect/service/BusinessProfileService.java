package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessDTO;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BusinessProfileService {

    BusinessDTO getBusinessProfileById(Long businessId);
    void addProduct(ProductRequest request);
    List<BusinessProfileResponse> searchBusinesses(String neighborhood, String businessName);
    List<ProductResponse> getProductsForCurrentBusiness();
    List<ProductResponse> getProductsByBusinessId(Long businessId);
    void deleteProduct(Long productId);
    void updateProduct(ProductRequest request, Long productId);
    List<BusinessDTO> getPublicBusinesses();
    void rateBusiness(Long businessId, Double ratingValue);
    Double calculateBusinessAverageRating(Long businessId);
    String saveBusinessPhoto(Long businessId, MultipartFile file);
}

