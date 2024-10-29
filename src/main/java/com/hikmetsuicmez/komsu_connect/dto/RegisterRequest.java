package com.hikmetsuicmez.komsu_connect.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String role,
        String neighborhood,
        ServiceProfileRequest serviceProfile
) {
}
