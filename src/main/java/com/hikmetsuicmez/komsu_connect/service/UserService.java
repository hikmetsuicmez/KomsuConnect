package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;

import java.util.List;

public interface UserService {

    User getCurrentUser();
    List<UserSummary> retrieveAllUsers();
    Object getCurrentUserProfile();
    Object getUserProfile(Long userId);
    BusinessProfileResponse updateUserProfile(UserProfileRequest userProfileRequest);
    void updateEmail(String newEmail);
}
