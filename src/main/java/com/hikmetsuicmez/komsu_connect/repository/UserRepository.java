package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import com.hikmetsuicmez.komsu_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.serviceProfile FROM User u WHERE u.neighborhood = :neighborhood")
    List<ServiceProfile> findServiceProfilesByNeighborhood(@Param("neighborhood") String neighborhood);
    Optional<User> findByEmail(String email);
}
