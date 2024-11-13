package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;

import java.util.List;

public class BusinessProfileMapper {

    public static BusinessProfileResponse mapToBusinessProfileResponse(BusinessProfile businessProfile) {

        List<String> productNames = businessProfile.getProducts()
                .stream()
                .map(Product::getName)
                .toList();

        return BusinessProfileResponse.builder()
                .businessName(businessProfile.getBusinessName())
                .businessDescription(businessProfile.getBusinessDescription())
                .products(productNames)
                .build();
    }

    public static List<BusinessProfileResponse> mapToBusinessProfileResponseList(List<BusinessProfile> businessProfiles) {
        return businessProfiles.stream()
                .map(BusinessProfileMapper::mapToBusinessProfileResponse)
                .toList();
    }
}
