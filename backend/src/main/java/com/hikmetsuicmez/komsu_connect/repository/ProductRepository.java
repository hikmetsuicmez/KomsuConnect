package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import com.hikmetsuicmez.komsu_connect.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByBusinessProfile(BusinessProfile businessProfile);

    @Query("SELECT p FROM Product p WHERE p.businessProfile = :businessProfile ORDER BY p.id DESC LIMIT 1")
    Product findLatestProductByBusinessProfile(@Param("businessProfile") BusinessProfile businessProfile);

}

