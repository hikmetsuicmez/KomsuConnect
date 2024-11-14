package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.BusinessProfileMapper;
import com.hikmetsuicmez.komsu_connect.mapper.UserMapper;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserSummary> retrieveAllUsers() {
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(UserMapper::toUserSummary)
                .toList();
    }

    @Override
    public Object getCurrentUserProfile() {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            throw new UserNotFoundException("User not found");
        }

        if (currentUser.getRole() == UserRole.ROLE_USER) {
            return userMapper.toUserProfileResponse(currentUser);
        } else if (currentUser.getRole() == UserRole.ROLE_BUSINESS_OWNER) {
            BusinessProfile businessProfile = currentUser.getBusinessProfile();
            if (businessProfile == null) {
                throw new NullPointerException("Business profile is null");
            }
            return BusinessProfileMapper.mapToBusinessProfileResponse(businessProfile);
        }

        throw new IllegalArgumentException("Invalid role type: " + currentUser.getRole());
    }


    @Override
    public UserProfileResponse updateUserProfile(UserProfileRequest userProfileRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found: " + loggedInUser.getId()));


        user.setFirstName(userProfileRequest.getFirstName());
        user.setLastName(userProfileRequest.getLastName());
        user.setEmail(userProfileRequest.getEmail());
        user.setNeighborhood(userProfileRequest.getNeighborhood());
        user.setPhoneNumber(userProfileRequest.getPhoneNumber());
        userRepository.save(user);


        return userMapper.toUserProfileResponse(user);
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }
}
