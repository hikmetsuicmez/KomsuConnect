package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.BusinessProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {


    List<BusinessProfile> findByUserNeighborhood(String neighborhood);

    List<BusinessProfile> findByBusinessNameContainingIgnoreCase(String businessName);

    List<BusinessProfile> findByUserNeighborhoodAndBusinessName(String neighborhood, String businessName);
}
