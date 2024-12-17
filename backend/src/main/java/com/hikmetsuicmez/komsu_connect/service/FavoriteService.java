package com.hikmetsuicmez.komsu_connect.service;

import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.FavoriteType;
import com.hikmetsuicmez.komsu_connect.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    List<FavoriteResponse> getFavoritesByUser(User user);
    void addFavoriteForBusiness(User user, Long businessId);
    void addFavoriteForProduct(User user, Long productId);
    void removeFavoriteForBusiness(User user, Long businessId);
    void removeFavoriteForProduct(User user, Long productId);
    void validateFavoriteNotExists(User user, Long targetId, FavoriteType favoriteType);
}
