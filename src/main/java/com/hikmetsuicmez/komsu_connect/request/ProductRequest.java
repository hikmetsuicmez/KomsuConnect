package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(max = 100, message = "Product name cannot exceed 100 characters.")
    private String name;
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private Double price;
}
