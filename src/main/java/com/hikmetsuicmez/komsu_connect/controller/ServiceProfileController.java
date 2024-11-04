package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;
import com.hikmetsuicmez.komsu_connect.service.ServiceProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-profiles")
@RequiredArgsConstructor
public class ServiceProfileController extends RestBaseController {

    private final ServiceProfileService serviceProfileService;

    @GetMapping
    public ApiResponse<List<ServiceProfileResponse>> getAllServiceProfiles() {
        List<ServiceProfileResponse> profiles = serviceProfileService.getAllServiceProfiles();
        return ApiResponse.success(profiles);
    }

    @GetMapping("/neighborhood")
    public ApiResponse<List<ServiceProfileResponse>> getNeighborhoodServiceProfiles(@RequestParam String neighborhood) {
        List<ServiceProfileResponse> profiles = serviceProfileService.getServiceProfilesByNeighborhood(neighborhood);
        return ApiResponse.success(profiles);
    }

    @GetMapping("/service-name")
    public ApiResponse<List<ServiceProfileResponse>> getServiceProfilesByServiceName(@RequestParam String serviceName) {
        List<ServiceProfileResponse> profiles = serviceProfileService.getServiceProfilesByServiceName(serviceName);
        return ApiResponse.success(profiles);
    }

    @GetMapping("/{id}")
    public ApiResponse<ServiceProfileResponse> getServiceProfileById(@PathVariable Long id) {
        ServiceProfileResponse serviceProfile = serviceProfileService.getServiceProfileById(id);
        return ApiResponse.success(serviceProfile);
    }

    @PostMapping
    public ApiResponse<ServiceProfileResponse> createServiceProfile(@Valid @RequestBody ServiceProfileRequest request) {
        ServiceProfileResponse createdServiceProfile = serviceProfileService.createServiceProfile(request);
        return ApiResponse.success(createdServiceProfile);
    }

    @PutMapping
    public ApiResponse<ServiceProfileResponse> updateServiceProfile(@RequestBody ServiceProfileRequest request) {
        ServiceProfileResponse updatedServiceProfile = serviceProfileService.updateServiceProfile(request);
        return ApiResponse.success(updatedServiceProfile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteServiceProfile() {
        serviceProfileService.deleteServiceProfile();
        return ResponseEntity.noContent().build();
    }
}
