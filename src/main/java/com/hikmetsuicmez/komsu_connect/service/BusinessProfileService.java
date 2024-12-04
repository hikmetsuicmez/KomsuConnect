package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessDTO;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;

import java.util.List;

public interface BusinessProfileService {

    void addProduct(ProductRequest request);
    List<BusinessProfileResponse> searchBusinesses(String neighborhood, String businessName);
    List<ProductResponse> getProductsForCurrentBusiness();
    List<ProductResponse> getProductsByBusinessId(Long businessId);
    void deleteProduct(Long productId);
    void updateProduct(ProductRequest request, Long productId);
    List<BusinessDTO> getPublicBusinesses();
}

