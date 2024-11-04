package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.UserUpdateRequest;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;

import java.util.List;

public interface UserService {

    List<UserSummary> retrieveAllUsers();
    UserSummary getCurrentUserProfile();
    UserSummary updateUserProfile(UserUpdateRequest userUpdateRequest);
}
