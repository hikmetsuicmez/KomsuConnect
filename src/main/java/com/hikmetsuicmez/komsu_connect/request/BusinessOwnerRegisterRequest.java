package com.hikmetsuicmez.komsu_connect.request;

import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BusinessOwnerRegisterRequest extends RegisterRequest {

    @NotBlank(message = "Business name is required.")
    @Size(min = 3, max = 50, message = "Business name must be between 3 and 50 characters.")
    private String businessName;

    @Size(max = 255, message = "Business description cannot exceed 255 characters.")
    private String businessDescription;

    BusinessOwnerRegisterRequest(@NotBlank(message = "First name cannot be blank.")
                                 String firstName, @NotBlank(message = "Last name cannot be blank.")
                                 String lastName, @NotBlank(message = "Email name cannot be blank.") @Email(message = "Email should be valid.")
                                 String email, @NotBlank(message = "Password name cannot be blank.")
                                 @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters.")
                                 String password, String phoneNumber, UserRole role, @NotBlank(message = "Neighborhood cannot be blank.") String neighborhood) {
        super(firstName, lastName, email, password, phoneNumber, role, neighborhood);
    }
}
