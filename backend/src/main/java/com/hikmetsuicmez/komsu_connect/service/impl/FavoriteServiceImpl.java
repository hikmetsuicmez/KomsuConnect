package com.hikmetsuicmez.komsu_connect.service.impl;

import com.hikmetsuicmez.komsu_connect.entity.Favorite;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.FavoriteType;
import com.hikmetsuicmez.komsu_connect.repository.FavoriteRepository;
import com.hikmetsuicmez.komsu_connect.response.FavoriteResponse;
import com.hikmetsuicmez.komsu_connect.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Override
    public List<FavoriteResponse> getFavoritesByUser(User user) {
        return favoriteRepository.findAllByUser(user).stream()
                .map(favorite -> new FavoriteResponse(
                        favorite.getId(),
                        favorite.getFavoriteTargetId(),
                        favorite.getFavoriteType(),
                        favorite.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void addFavoriteForBusiness(User user, Long businessId) {
        validateFavoriteNotExists(user, businessId, FavoriteType.BUSINESS);
        Favorite favorite = Favorite.builder()
                .user(user)
                .favoriteTargetId(businessId)
                .favoriteType(FavoriteType.BUSINESS)
                .createdAt(LocalDateTime.now())
                .build();
        favoriteRepository.save(favorite);
    }

    @Override
    public void addFavoriteForProduct(User user, Long productId) {
        validateFavoriteNotExists(user, productId, FavoriteType.PRODUCT);
        Favorite favorite = Favorite.builder()
                .user(user)
                .favoriteTargetId(productId)
                .favoriteType(FavoriteType.PRODUCT)
                .createdAt(LocalDateTime.now())
                .build();
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavoriteForBusiness(User user, Long businessId) {
        Favorite favorite = favoriteRepository.findByUserAndFavoriteTargetIdAndFavoriteType(user, businessId, FavoriteType.BUSINESS)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found for Business"));
        favoriteRepository.delete(favorite);
    }

    @Override
    public void removeFavoriteForProduct(User user, Long productId) {
        Favorite favorite = favoriteRepository.findByUserAndFavoriteTargetIdAndFavoriteType(user, productId, FavoriteType.PRODUCT)
                .orElseThrow(() -> new IllegalArgumentException("Favorite not found for Product"));
        favoriteRepository.delete(favorite);
    }

    @Override
    public void validateFavoriteNotExists(User user, Long targetId, FavoriteType favoriteType) {
        boolean exists = favoriteRepository.existsByUserAndFavoriteTargetIdAndFavoriteType(user, targetId, favoriteType);
        if (exists) {
            throw new IllegalArgumentException("Favorite already exists for this " + favoriteType);
        }
    }

}
