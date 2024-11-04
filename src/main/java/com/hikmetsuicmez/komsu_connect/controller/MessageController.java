package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController extends RestBaseController {

    private final MessageService messageService;

    @PostMapping("/send/{receiverId}")
    public ApiResponse<MessageResponse> sendMessage(@PathVariable Long receiverId, @Valid @RequestBody String content) {
        return success(messageService.sendMessage(receiverId, content));
    }

    @GetMapping("/history/{userId}")
    public ApiResponse<List<MessageResponse>> getMessageHistory(@PathVariable Long userId) {
        return success(messageService.getMessageHistory(userId));
    }

    @GetMapping("/inbox")
    public ApiResponse<List<MessageResponse>> getInbox() {
        return success(messageService.getInboxMessages());
    }

    @PutMapping("/read/{messageId}/")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/conversation/{userId}/{selectedUserId}")
    public ApiResponse<List<MessageResponse>> getConversation(
            @PathVariable Long userId,
            @PathVariable Long selectedUserId) {
        List<MessageResponse> messages = messageService.getConversationBetweenUsers(userId, selectedUserId);
        return success(messages);
    }

}
