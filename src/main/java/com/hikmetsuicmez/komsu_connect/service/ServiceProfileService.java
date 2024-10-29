package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;

import java.util.List;

public interface ServiceProfileService {

    List<ServiceProfile> getAllServiceProfiles();
    ServiceProfile getServiceProfileById(Long id);
    ServiceProfile createServiceProfile(ServiceProfile serviceProfile);
    ServiceProfile updateServiceProfile(ServiceProfile serviceProfile);
    void deleteServiceProfile();
}
