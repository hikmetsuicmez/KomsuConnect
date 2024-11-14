package com.hikmetsuicmez.komsu_connect.request;

import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RegisterRequest {

        @NotBlank(message = "First name cannot be blank.")
        private String firstName;
        @NotBlank(message = "Last name cannot be blank.")
        private String lastName;

        @NotBlank(message = "Email name cannot be blank.")
        @Email(message = "Email should be valid.")
        private String email;

        @NotBlank(message = "Password name cannot be blank.")
        @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters.")
        private String password;

        private String phoneNumber;

        private UserRole role;

        @NotBlank(message = "Neighborhood cannot be blank.")
        private String neighborhood;

        @NotBlank(message = "Business name is required.")
        @Size(min = 3, max = 50, message = "Business name must be between 3 and 50 characters.")
        private String businessName; // Sadece iş sahibi için

        @Size(max = 255, message = "Business description cannot exceed 255 characters.")
        private String businessDescription;

}
