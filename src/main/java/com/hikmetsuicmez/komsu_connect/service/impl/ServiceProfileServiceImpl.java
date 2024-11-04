package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.ServiceProfileMapper;
import com.hikmetsuicmez.komsu_connect.repository.ServiceProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;
import com.hikmetsuicmez.komsu_connect.service.ServiceProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProfileServiceImpl implements ServiceProfileService {

    private final ServiceProfileRepository serviceProfileRepository;
    private final UserRepository userRepository;

    @Override
    public List<ServiceProfileResponse> getAllServiceProfiles() {
        List<ServiceProfile> profiles = serviceProfileRepository.findAll();
        return profiles
                .stream()
                .map(ServiceProfileMapper::toResponse)
                .toList();
    }

    @Override
    public List<ServiceProfileResponse> getServiceProfilesByNeighborhood(String neighborhood) {
        List<ServiceProfile> profiles = userRepository.findServiceProfilesByNeighborhood(neighborhood);
        return profiles
                .stream()
                .map(ServiceProfileMapper::toResponse)
                .toList();

    }

    @Override
    public List<ServiceProfileResponse> getServiceProfilesByServiceName(String serviceName) {
        List<ServiceProfile> profiles = serviceProfileRepository.findByServiceName(serviceName);
        return profiles
                .stream()
                .map(ServiceProfileMapper::toResponse)
                .toList();
    }

    @Override
    public ServiceProfileResponse getServiceProfileById(Long id) {
        ServiceProfile serviceProfile = serviceProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Service profile not found with id: " + id));
        return ServiceProfileMapper.toResponse(serviceProfile);
    }

    @Override
    public ServiceProfileResponse createServiceProfile(ServiceProfileRequest request) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getServiceProfile() != null) {
            throw new RuntimeException("User already has a service profile. Only one profile is allowed per user.");
        }

        ServiceProfile newServiceProfile = ServiceProfileMapper.toEntity(request);
        user.setServiceProfile(newServiceProfile);
        serviceProfileRepository.save(newServiceProfile);
        userRepository.save(user);

        return ServiceProfileMapper.toResponse(newServiceProfile);
    }

    @Override
    public ServiceProfileResponse updateServiceProfile(ServiceProfileRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getServiceProfile() == null) {
            throw new RuntimeException("No permission to update this profile.");
        }

        ServiceProfile existingProfile = user.getServiceProfile();
        existingProfile.setServiceName(request.getServiceName());
        existingProfile.setDescription(request.getDescription());
        serviceProfileRepository.save(existingProfile);

        return ServiceProfileMapper.toResponse(existingProfile);
    }

    @Override
    public void deleteServiceProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getServiceProfile() != null) {
            ServiceProfile profileToDelete = user.getServiceProfile();
            user.setServiceProfile(null);
            userRepository.save(user);
            serviceProfileRepository.delete(profileToDelete);
        } else {
            throw new RuntimeException("No service profile found to delete.");
        }
    }
}
