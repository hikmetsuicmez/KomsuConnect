package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.request.ProductRequest;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.service.BusinessProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessProfileController extends RestBaseController {

    private final BusinessProfileService businessProfileService;

    @PostMapping("/add-product")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ApiResponse<String> addProduct(@RequestBody @Valid ProductRequest request) {
        businessProfileService.addProduct(request);
        return ApiResponse.success("Product added successfully");
    }
}
