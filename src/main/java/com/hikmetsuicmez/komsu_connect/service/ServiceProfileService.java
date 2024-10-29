package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;

import java.util.List;

public interface ServiceProfileService {

    List<ServiceProfileResponse> getAllServiceProfiles();
    ServiceProfileResponse getServiceProfileById(Long id);
    ServiceProfileResponse createServiceProfile(ServiceProfileRequest request);
    ServiceProfileResponse updateServiceProfile(ServiceProfileRequest request);
    void deleteServiceProfile();
}
