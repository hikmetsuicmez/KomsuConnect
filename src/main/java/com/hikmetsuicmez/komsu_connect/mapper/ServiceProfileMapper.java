package com.hikmetsuicmez.komsu_connect.mapper;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;

public class ServiceProfileMapper {

    // Entity to DTO mapping
    public static ServiceProfileResponse toResponse(ServiceProfile serviceProfile) {
        return ServiceProfileResponse.builder()
                .id(serviceProfile.getId())
                .description(serviceProfile.getDescription())
                .serviceName(serviceProfile.getServiceName())
                .build();
    }

    // DTO to Entity mapping
    public static ServiceProfile toEntity(ServiceProfileRequest request) {
        return ServiceProfile.builder()
                .serviceName(request.serviceName())
                .description(request.description())
                .build();
    }
}
