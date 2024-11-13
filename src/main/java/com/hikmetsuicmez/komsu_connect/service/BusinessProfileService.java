package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;

import java.util.List;

public interface BusinessProfileService {

    void addProduct(ProductRequest request);
    List<BusinessProfileResponse> searchBusinesses(String neighborhood, String businessName);
}
