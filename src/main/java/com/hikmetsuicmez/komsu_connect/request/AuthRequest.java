package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(


        @NotBlank(message = "Email must not be blank")
        String email,

        @NotBlank(message = "Password must not be blank")
        String password
) {
}
