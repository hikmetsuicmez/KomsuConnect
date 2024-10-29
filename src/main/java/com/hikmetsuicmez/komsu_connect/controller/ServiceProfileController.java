package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.service.ServiceProfileService;
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
    public ResponseEntity<List<ServiceProfile>> getAllServiceProfiles() {
        List<ServiceProfile> serviceProfiles = serviceProfileService.getAllServiceProfiles();
        return ResponseEntity.ok(serviceProfiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProfile> getServiceProfileById(@PathVariable Long id) {
        ServiceProfile serviceProfile = serviceProfileService.getServiceProfileById(id);
        return ResponseEntity.ok(serviceProfile);
    }

    @PostMapping
    public ResponseEntity<ServiceProfile> createServiceProfile(@RequestBody ServiceProfile serviceProfile) {
        ServiceProfile createdServiceProfile = serviceProfileService.createServiceProfile(serviceProfile);
        return new ResponseEntity<>(createdServiceProfile, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ServiceProfile> updateServiceProfile(@RequestBody ServiceProfile serviceProfile) {
        ServiceProfile updatedServiceProfile = serviceProfileService.updateServiceProfile(serviceProfile);
        return ResponseEntity.ok(updatedServiceProfile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteServiceProfile() {
        serviceProfileService.deleteServiceProfile();
        return ResponseEntity.noContent().build();
    }
}
