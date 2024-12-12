package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.entity.User;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiver(User sender, User receiver);

    List<Message> findBySenderAndReceiverOrReceiverAndSender(User sender, User receiver, User receiverAlt, User senderAlt);

    List<Message> findByReceiver(User receiver);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId AND m.receiver.id = :selectedUserId)" +
            "OR (m.sender.id = :selectedUserId AND m.receiver.id = :userId)" +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversationBetweenUsers(@Param("userId") Long userId, @Param("selectedUserId") Long selectedUserId);


}


