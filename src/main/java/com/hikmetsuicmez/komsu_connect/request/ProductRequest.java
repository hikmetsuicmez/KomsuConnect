package com.hikmetsuicmez.komsu_connect.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(max = 100, message = "Product name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Description must not be blank")
    @Size(max = 300, message = "Product name cannot exceed 300 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    @Positive(message = "Price must be greater than 0")
    @NotNull(message = "Price must not be null")
    private Double price;
}
