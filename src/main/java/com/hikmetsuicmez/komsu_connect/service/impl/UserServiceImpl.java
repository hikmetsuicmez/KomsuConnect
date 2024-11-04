package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.UserMapper;
import com.hikmetsuicmez.komsu_connect.repository.ServiceProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.request.UserUpdateRequest;
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
    private final ServiceProfileRepository serviceProfileRepository;

    @Override
    public List<UserSummary> retrieveAllUsers() {
        List<User> users = userRepository.findAll();
        return users
                .stream()
                .map(UserMapper::toUserSummary)
                .toList();
    }

    @Override
    public UserSummary getCurrentUserProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return UserMapper.toUserSummary(user);
    }


    @Override
    public UserSummary updateUserProfile(UserUpdateRequest userUpdateRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found: " + loggedInUser.getId()));

        user.setFirstName(userUpdateRequest.firstName());
        user.setLastName(userUpdateRequest.lastName());
        user.setEmail(userUpdateRequest.email());
        user.setNeighborhood(userUpdateRequest.neighborhood());
        user.setPhoneNumber(userUpdateRequest.phoneNumber());

        ServiceProfile serviceProfile = user.getServiceProfile();
        serviceProfile.setServiceName(userUpdateRequest.serviceProfileRequest().serviceName());
        serviceProfile.setDescription(userUpdateRequest.serviceProfileRequest().description());

        serviceProfileRepository.save(serviceProfile);
        userRepository.save(user);

        return UserMapper.toUserSummary(user);
    }
}
