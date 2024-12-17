package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.Favorite;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.enums.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndFavoriteTargetIdAndFavoriteType(User user, Long targetId, FavoriteType favoriteType);

    boolean existsByUserAndFavoriteTargetIdAndFavoriteType(User user, Long targetId, FavoriteType favoriteType);

    List<Favorite> findAllByUser(User user);
}

