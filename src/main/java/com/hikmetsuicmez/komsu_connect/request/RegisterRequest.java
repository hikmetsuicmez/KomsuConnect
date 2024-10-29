package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "First name cannot be blank.")
        String firstName,

        @NotBlank(message = "Last name cannot be blank.")
        String lastName,

        @NotBlank(message = "Email name cannot be blank.")
        @Email(message = "Email should be valid.")
        String email,

        @NotBlank(message = "Password name cannot be blank.")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters.")
        String password,

        String role,

        @NotBlank(message = "Neighborhood cannot be blank.")
        String neighborhood,

        ServiceProfileRequest serviceProfile
) {
}
