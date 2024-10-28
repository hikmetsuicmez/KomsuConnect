package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.dto.AuthRequest;
import com.hikmetsuicmez.komsu_connect.dto.AuthResponse;
import com.hikmetsuicmez.komsu_connect.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);
    AuthResponse login(AuthRequest request);
}
