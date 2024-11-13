package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.UserProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends RestBaseController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserSummary>> retrieveAllUsers() {
        List<UserSummary> users = userService.retrieveAllUsers();
        return ApiResponse.success(users);
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentUser() {
        UserProfileResponse response = userService.getCurrentUserProfile();
        return ApiResponse.success(response);
    }


    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> updateUser(@RequestBody UserProfileRequest request) {
        UserProfileResponse response = userService.updateUserProfile(request);
        return ApiResponse.success(response);
    }

}
