package com.hikmetsuicmez.komsu_connect.repository;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiver(User sender, User receiver);
    List<Message> findBySenderAndReceiverOrReceiverAndSender(User sender, User receiver, User receiverAlt, User senderAlt);
    List<Message> findByReceiver(User receiver);
}
