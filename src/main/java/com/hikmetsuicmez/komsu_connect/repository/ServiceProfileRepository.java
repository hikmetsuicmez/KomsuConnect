package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.ServiceProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceProfileRepository extends JpaRepository<ServiceProfile, Long> {

    List<ServiceProfile> findByServiceName(String serviceName);
}
