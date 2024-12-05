package com.hikmetsuicmez.komsu_connect.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hikmetsuicmez.komsu_connect.config.RegisterRequestDeserializer;
import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
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

        public RegisterRequest(String firstName, String lastName, String email, String password, String phoneNumber, UserRole role, String neighborhood) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.password = password;
                this.phoneNumber = phoneNumber;
                this.role = role;
                this.neighborhood = neighborhood;
        }

}
