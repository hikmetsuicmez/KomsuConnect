package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.request.AuthRequest;
import com.hikmetsuicmez.komsu_connect.response.AuthResponse;
import com.hikmetsuicmez.komsu_connect.request.RegisterRequest;
import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.repository.ServiceProfileRepository;
import com.hikmetsuicmez.komsu_connect.repository.UserRepository;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
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
    private final ServiceProfileRepository serviceProfileRepository;
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
                .phoneNumber(request.getPhoneNumber())
                .neighborhood(request.getNeighborhood())
                .role("ROLE_USER")
                .enabled(true)
                .build();

        if (request.getServiceProfile() != null) {
            ServiceProfile serviceProfile = ServiceProfile.builder()
                    .serviceName(request.getServiceProfile().getServiceName())
                    .description(request.getServiceProfile().getDescription())
                    .build();
            user.setServiceProfile(serviceProfile);
            serviceProfileRepository.save(serviceProfile);
        }
        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.email(), request.password());
            authenticationProvider.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        Optional<User> user = userRepository.findByEmail(request.email());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        String token = jwtService.generateToken(user.get());
        UserSummary userSummary = UserSummary.builder()
                .id(user.get().getId())
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .build();

        return new AuthResponse(token, "Login successful", userSummary);
    }


    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
