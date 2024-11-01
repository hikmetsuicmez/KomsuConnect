package com.hikmetsuicmez.komsu_connect.response;

public record AuthResponse(
        String token,
        String message,
        UserSummary userSummary
) {

}
