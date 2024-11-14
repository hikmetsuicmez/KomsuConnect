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

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequest request) {
        if (request instanceof BusinessOwnerRegisterRequest) {
            authService.registerBusinessOwner((BusinessOwnerRegisterRequest) request);
        } else {
            authService.register(request);
        }
        return ApiResponse.success("Registration successful.");
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ApiResponse.success(authResponse);
    }
}
