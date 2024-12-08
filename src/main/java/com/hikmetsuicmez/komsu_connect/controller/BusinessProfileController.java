package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.BusinessDTO;
import com.hikmetsuicmez.komsu_connect.response.BusinessProfileResponse;
import com.hikmetsuicmez.komsu_connect.response.ProductResponse;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessProfileController extends RestBaseController {

    private final BusinessProfileService businessProfileService;

    @GetMapping("/search")
    public ApiResponse<List<BusinessProfileResponse>> searchBusinesses(
            @RequestParam(required = false) String neighborhood,
            @RequestParam(required = false) String businessName) {

        List<BusinessProfileResponse> results = businessProfileService.searchBusinesses(neighborhood, businessName);
        System.out.println("Gelen Neighborhood Parametresi: " + neighborhood);
        System.out.println("Gelen businessName Parametresi: " + businessName);

        return ApiResponse.success(results);
    }

    @GetMapping("/{businessId}")
    public ApiResponse<BusinessDTO> getBusinessById(@PathVariable Long businessId) {
        BusinessDTO businessDTO = businessProfileService.getBusinessProfileById(businessId);
        return ApiResponse.success(businessDTO);
    }


    @PostMapping("/add-product")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ApiResponse<String> addProduct(@RequestBody @Valid ProductRequest request) {
        businessProfileService.addProduct(request);
        return ApiResponse.success("Product added successfully");
    }

    @GetMapping("/products")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ApiResponse<List<ProductResponse>> getProducts() {
        List<ProductResponse> products = businessProfileService.getProductsForCurrentBusiness();
        return ApiResponse.success(products);
    }

    @GetMapping("/public-businesses")
    public ApiResponse<List<BusinessDTO>> getAllBusinesses() {
        List<BusinessDTO> businesses = businessProfileService.getPublicBusinesses();
        return ApiResponse.success(businesses);
    }

    @PostMapping("/rate")
    public ApiResponse<String> rateBusiness(
            @RequestParam("businessId") @Valid @NotNull @Positive(message = "Invalid business ID") Long businessId,
            @RequestParam("rating") @Valid @NotNull @DecimalMin("0.0") @DecimalMax("5.0") Double rating) {
        businessProfileService.rateBusiness(businessId, rating);
        return ApiResponse.success("Rate successfully");
    }

    @GetMapping("/{businessId}/average-rating")
    public ApiResponse<Double> getAverageRating(@PathVariable Long businessId) {
        Double averageRating = businessProfileService.calculateBusinessAverageRating(businessId);
        return ApiResponse.success(averageRating);
    }


    @GetMapping("/{businessId}/products")
    public ApiResponse<List<ProductResponse>> getProductsByBusinessId(@PathVariable Long businessId) {
        List<ProductResponse> products = businessProfileService.getProductsByBusinessId(businessId);
        return ApiResponse.success(products);
    }

    @PutMapping("/products/{productId}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ApiResponse<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductRequest request) {
        businessProfileService.updateProduct(request, productId);
        return ApiResponse.success("Product updated successfully");
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ApiResponse<String> deleteProduct(@PathVariable Long productId) {
        businessProfileService.deleteProduct(productId);
        return ApiResponse.success("Product deleted successfully");
    }

}

