package com.hikmetsuicmez.komsu_connect.request;

import com.hikmetsuicmez.komsu_connect.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class BusinessOwnerRegisterRequest extends RegisterRequest {

    @NotBlank(message = "Business name is required.")
    @Size(min = 3, max = 50, message = "Business name must be between 3 and 50 characters.")
    private String businessName;

    @Size(max = 255, message = "Business description cannot exceed 255 characters.")
    private String businessDescription;


    public BusinessOwnerRegisterRequest(String firstName, String lastName, String email, String password, String phoneNumber, UserRole role, String neighborhood, String businessName, String businessDescription) {
        super(firstName, lastName, email, password, phoneNumber, role, neighborhood);
        this.businessName = businessName;
        this.businessDescription = businessDescription;
    }
}
