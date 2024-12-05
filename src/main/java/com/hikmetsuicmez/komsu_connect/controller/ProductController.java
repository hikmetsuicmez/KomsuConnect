package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController extends RestBaseController {

    private final ProductService productService;

    @PostMapping("/{productId}/rate")
    public ApiResponse<String> rateProduct(
            @PathVariable Long productId,
            @RequestParam Double rating) {
        productService.rateProduct(productId, rating);
        return ApiResponse.success("The point was awarded successfully.");
    }
}
