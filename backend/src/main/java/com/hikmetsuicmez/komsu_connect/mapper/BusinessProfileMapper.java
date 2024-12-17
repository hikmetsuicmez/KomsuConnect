package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;

import java.util.List;

public class BusinessProfileMapper {

    public static BusinessProfileResponse mapToBusinessProfileResponse(BusinessProfile businessProfile) {

        return BusinessProfileResponse.builder()
                .id(businessProfile.getId())
                .firstName(businessProfile.getUser().getFirstName())
                .lastName(businessProfile.getUser().getLastName())
                .email(businessProfile.getUser().getEmail())
                .phoneNumber(businessProfile.getUser().getPhoneNumber())
                .neighborhood(businessProfile.getUser().getNeighborhood())
                .businessName(businessProfile.getBusinessName())
                .businessDescription(businessProfile.getBusinessDescription())
                .photoUrl(businessProfile.getPhotoUrl())
                .products(businessProfile.getProducts().stream()
                        .map(product -> ProductResponse.builder()
                                .name(product.getName())
                                .description(product.getDescription())
                                .price(product.getPrice())
                                .build())
                        .toList())
                .build();
    }

    public static List<BusinessProfileResponse> mapToBusinessProfileResponseList(List<BusinessProfile> businessProfiles) {
        return businessProfiles.stream()
                .map(BusinessProfileMapper::mapToBusinessProfileResponse)
                .toList();
    }
}
