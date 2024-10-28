package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.dto.AuthRequest;
import com.hikmetsuicmez.komsu_connect.dto.AuthResponse;
import com.hikmetsuicmez.komsu_connect.dto.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.security.JwtService;
import com.hikmetsuicmez.komsu_connect.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;


    @Override
    public String register(RegisterRequest request) {
        if (emailExists(request.getEmail())) {
            throw new IllegalArgumentException("There is an account with that email address: "
                    + request.getEmail());
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .neighborhood(request.getNeighborhood())
                .profession(request.getProfession())
                .role("ROLE_USER")
                .enabled(true)
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            authenticationProvider.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        String token = jwtService.generateToken(user.get());
        return new AuthResponse(token, "Login successful");
    }


    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
