package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.request.AuthRequest;
import com.hikmetsuicmez.komsu_connect.request.BusinessOwnerRegisterRequest;
import com.hikmetsuicmez.komsu_connect.response.AuthResponse;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);
    String registerBusinessOwner(BusinessOwnerRegisterRequest request);
    AuthResponse login(AuthRequest request);
}
