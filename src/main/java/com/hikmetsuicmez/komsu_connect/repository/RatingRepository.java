package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Rating;
import com.hikmetsuicmez.komsu_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.business.id = :businessId")
    Double calculateAverageRatingForBusiness(@Param("businessId") Long businessId);

    @Query("SELECT r FROM Rating r WHERE r.business = :business AND r.user = :user")
    Optional<Rating> findByBusinessAndUser(@Param("business") BusinessProfile business, @Param("user") User user);
}
