package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.FavoriteResponse;
import com.hikmetsuicmez.komsu_connect.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController extends RestBaseController {

    private final FavoriteService favoriteService;

    // Kullanıcının favori işletmelerini ve ürünlerini listeleme
    @GetMapping
    public ApiResponse<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal User user) {
        List<FavoriteResponse> favorites = favoriteService.getFavoritesByUser(user);
        return ApiResponse.success(favorites);
    }

    // İşletmeyi favorilere ekleme
    @PostMapping("/business/{businessId}")
    public ApiResponse<String> addFavoriteToBusiness(@AuthenticationPrincipal User user, @PathVariable Long businessId) {
        favoriteService.addFavoriteForBusiness(user, businessId);
        return ApiResponse.success("Favori işletme eklendi.");
    }

    // Ürünü favorilere ekleme
    @PostMapping("/product/{productId}")
    public ApiResponse<String> addFavoriteToProduct(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        favoriteService.addFavoriteForProduct(user,productId);
        return ApiResponse.success("Favori ürün eklendi.");
    }

    // Favori işletmeyi kaldırma
    @DeleteMapping("/business/{businessId}")
    public ApiResponse<String> removeFavoriteFromBusiness(@AuthenticationPrincipal User user, @PathVariable Long businessId) {
        favoriteService.removeFavoriteForBusiness(user, businessId);
        return ApiResponse.success("Favori işletme kaldırıldı.");
    }

    // Favori ürünü kaldırma
    @DeleteMapping("/product/{productId}")
    public ApiResponse<String> removeFavoriteFromProduct(@AuthenticationPrincipal User user, @PathVariable Long productId) {
        favoriteService.removeFavoriteForProduct(user, productId);
        return ApiResponse.success("Favori ürün kaldırıldı.");
    }





}
