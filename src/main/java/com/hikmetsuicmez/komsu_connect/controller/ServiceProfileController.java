package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.request.ServiceProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ServiceProfileResponse;
import com.hikmetsuicmez.komsu_connect.service.ServiceProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-profiles")
@RequiredArgsConstructor
public class ServiceProfileController {

    private final ServiceProfileService serviceProfileService;

    @GetMapping
    public ResponseEntity<List<ServiceProfileResponse>> getAllServiceProfiles() {
        List<ServiceProfileResponse> profiles = serviceProfileService.getAllServiceProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/neighborhood")
    public ResponseEntity<List<ServiceProfileResponse>> getNeighborhoodServiceProfiles(@RequestParam String neighborhood) {
        List<ServiceProfileResponse> profiles = serviceProfileService.getServiceProfilesByNeighborhood(neighborhood);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/service-name")
    public ResponseEntity<List<ServiceProfileResponse>> getServiceProfilesByServiceName(@RequestParam String serviceName) {
        List<ServiceProfileResponse> profiles = serviceProfileService.getServiceProfilesByServiceName(serviceName);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProfileResponse> getServiceProfileById(@PathVariable Long id) {
        ServiceProfileResponse serviceProfile = serviceProfileService.getServiceProfileById(id);
        return ResponseEntity.ok(serviceProfile);
    }

    @PostMapping
    public ResponseEntity<ServiceProfileResponse> createServiceProfile(@Valid @RequestBody ServiceProfileRequest request) {
        ServiceProfileResponse createdServiceProfile = serviceProfileService.createServiceProfile(request);
        return new ResponseEntity<>(createdServiceProfile, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ServiceProfileResponse> updateServiceProfile(@RequestBody ServiceProfileRequest request) {
        ServiceProfileResponse updatedServiceProfile = serviceProfileService.updateServiceProfile(request);
        return ResponseEntity.ok(updatedServiceProfile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteServiceProfile() {
        serviceProfileService.deleteServiceProfile();
        return ResponseEntity.noContent().build();
    }
}
