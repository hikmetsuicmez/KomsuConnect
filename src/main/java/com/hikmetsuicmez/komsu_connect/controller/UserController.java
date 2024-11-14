package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.mapper.UserMapper;
import com.hikmetsuicmez.komsu_connect.request.EmailUpdateRequest;
import com.hikmetsuicmez.komsu_connect.request.UserProfileRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
import com.hikmetsuicmez.komsu_connect.security.JwtService;
import com.hikmetsuicmez.komsu_connect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends RestBaseController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping
    public ApiResponse<List<UserSummary>> retrieveAllUsers() {
        List<UserSummary> users = userService.retrieveAllUsers();
        return ApiResponse.success(users);
    }

    @GetMapping("/me")
    public ApiResponse<?> getCurrentUser() {
        return ApiResponse.success(userService.getCurrentUserProfile());
    }

    @GetMapping("/{userId}")
    public ApiResponse<Object> getUserProfile(@PathVariable Long userId) {
        return ApiResponse.success(userService.getUserProfile(userId));
    }


    @PutMapping("/me")
    public ApiResponse<BusinessProfileResponse> updateUser(@RequestBody UserProfileRequest request) {
        BusinessProfileResponse response = userService.updateUserProfile(request);
        return ApiResponse.success(response);
    }

    @PutMapping("/update-email")
    public ApiResponse<String> updateEmail(@RequestBody EmailUpdateRequest request) {
        userService.updateEmail(request.getNewEmail());
        return ApiResponse.success("Email updated successfully. Please login again.");
    }

}
