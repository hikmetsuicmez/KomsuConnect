package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.UserUpdateRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
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
    public ApiResponse<UserSummary> getCurrentUser() {
        UserSummary userSummary = userService.getCurrentUserProfile();
        return ApiResponse.success(userSummary);
    }


    @PutMapping("/me")
    public ApiResponse<UserSummary> updateUser(@RequestBody UserUpdateRequest request) {
        UserSummary userSummary = userService.updateUserProfile(request);
        return ApiResponse.success(userSummary);
    }

}
