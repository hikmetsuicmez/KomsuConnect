package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.exception.UserNotFoundException;
import com.hikmetsuicmez.komsu_connect.mapper.UserMapper;
import com.hikmetsuicmez.komsu_connect.repository.ServiceProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ServiceProfileRepository serviceProfileRepository;
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
    public UserProfileResponse getCurrentUserProfile() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return userMapper.toUserProfileResponse(user);
    }


    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UserProfileRequest userProfileRequest) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User Not Found: " + loggedInUser.getId()));

        Hibernate.initialize(user.getServiceProfile());

        user.setFirstName(userProfileRequest.getFirstName());
        user.setLastName(userProfileRequest.getLastName());
        user.setEmail(userProfileRequest.getEmail());
        user.setNeighborhood(userProfileRequest.getNeighborhood());
        user.setPhoneNumber(userProfileRequest.getPhoneNumber());
        userRepository.save(user);


        return userMapper.toUserProfileResponse(user);
    }
}
