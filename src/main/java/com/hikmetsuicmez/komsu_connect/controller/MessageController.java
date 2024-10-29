package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.entity.Message;
import com.hikmetsuicmez.komsu_connect.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send/{receiverId}")
    public Message sendMessage(@PathVariable @Valid Long receiverId, @RequestBody String content) {
        return messageService.sendMessage(receiverId, content);
    }

    @GetMapping("/history/{userId}")
    public List<Message> getMessageHistory(@PathVariable Long userId) {
        return messageService.getMessageHistory(userId);
    }
}
