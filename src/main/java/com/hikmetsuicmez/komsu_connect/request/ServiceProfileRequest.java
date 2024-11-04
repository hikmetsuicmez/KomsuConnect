package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ServiceProfileRequest {

        @NotBlank(message = "Service name cannot be blank.")
        @Size(min = 2, max = 50, message = "Service name must be between 2 and 50 characters.")
        private String serviceName;

        @NotBlank(message = "Description cannot be blank.")
        @Size(max = 200, message = "Description must be at most 200 characters.")
        private String description;

}

