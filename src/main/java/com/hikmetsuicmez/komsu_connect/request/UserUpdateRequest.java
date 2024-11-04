package com.hikmetsuicmez.komsu_connect.request;

import lombok.Builder;

@Builder
public record UserUpdateRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String neighborhood,
        ServiceProfileRequest serviceProfileRequest
) {
}
