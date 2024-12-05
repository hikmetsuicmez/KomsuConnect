package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.Notification;
import com.hikmetsuicmez.komsu_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserAndIsReadFalseOrderByTimestampDesc(User user);

    int countByUserAndIsReadFalse(User currentUser);
}
