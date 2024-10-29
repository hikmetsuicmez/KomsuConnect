package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.repository.ServiceProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
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
    public List<ServiceProfile> getAllServiceProfiles() {
        return serviceProfileRepository.findAll();
    }

    @Override
    public ServiceProfile getServiceProfileById(Long id) {
        return serviceProfileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service profile not found with id: " + id));
    }

    @Override
    public ServiceProfile createServiceProfile(ServiceProfile serviceProfile) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getServiceProfile() != null) {
            throw new RuntimeException("User already has a service profile. Only one profile is allowed per user.");
        }
        user.setServiceProfile(serviceProfile);
        serviceProfileRepository.save(serviceProfile);
        userRepository.save(user);

        return serviceProfile;
    }

    @Override
    public ServiceProfile updateServiceProfile(ServiceProfile updatedProfile) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kullanıcının mevcut profili olup olmadığını kontrol ediyoruz
        if (user.getServiceProfile() == null || !user.getServiceProfile().getId().equals(updatedProfile.getId())) {
            throw new RuntimeException("No permission to update this profile.");
        }

        // Güncellemeyi uyguluyoruz
        ServiceProfile existingProfile = user.getServiceProfile();
        existingProfile.setServiceName(updatedProfile.getServiceName());
        existingProfile.setDescription(updatedProfile.getDescription());

        return serviceProfileRepository.save(existingProfile);
    }

    @Override
    public void deleteServiceProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Kullanıcının profili olup olmadığını kontrol ediyoruz
        if (user.getServiceProfile() != null) {
            // Kullanıcının profil ilişkisini kaldırıyoruz
            ServiceProfile profileToDelete = user.getServiceProfile();
            user.setServiceProfile(null); // User tablosundaki ilişkiyi kaldır
            userRepository.save(user); // User kaydını güncelliyoruz

            // ServiceProfile kaydını siliyoruz
            serviceProfileRepository.delete(profileToDelete);
        } else {
            throw new RuntimeException("No service profile found to delete.");
        }
    }
}
