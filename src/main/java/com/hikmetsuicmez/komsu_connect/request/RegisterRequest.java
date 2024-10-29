package com.hikmetsuicmez.komsu_connect.request;

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
