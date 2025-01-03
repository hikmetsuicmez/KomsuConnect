package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.AuthRequest;
import com.hikmetsuicmez.komsu_connect.request.BusinessOwnerRegisterRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.AuthResponse;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends RestBaseController {

    private final AuthService authService;

    @PostMapping("/register/user")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ApiResponse.success("Registration successful.");
    }

    @PostMapping("/register/business")
    public ApiResponse<String> registerBusiness(@Valid @RequestBody BusinessOwnerRegisterRequest request) {
        authService.registerBusinessOwner(request);
        return ApiResponse.success("Registration successful.");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ApiResponse.success(authResponse);
    }
}
