package com.hikmetsuicmez.komsu_connect.controller;

import com.hikmetsuicmez.komsu_connect.controller.base.RestBaseController;
import com.hikmetsuicmez.komsu_connect.response.ApiResponse;
import com.hikmetsuicmez.komsu_connect.response.MessageDTO;
import com.hikmetsuicmez.komsu_connect.response.MessageResponse;
import com.hikmetsuicmez.komsu_connect.response.UserSummary;
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
    public ApiResponse<MessageDTO> sendMessage(@PathVariable Long receiverId, @Valid @RequestBody String content) {
        MessageDTO response = messageService.sendMessage(receiverId, content);
        return ApiResponse.success(response);
    }

    @GetMapping("/history/{userId}")
    public ApiResponse<List<MessageResponse>> getMessageHistory(@PathVariable Long userId) {
        return ApiResponse.success(messageService.getMessageHistory(userId));
    }

    @GetMapping("/inbox")
    public ApiResponse<List<MessageResponse>> getInbox() {
        return ApiResponse.success(messageService.getInboxMessages());
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

        if (messages.isEmpty()) {
            MessageResponse emptyConversation = MessageResponse.builder()
                    .id(null)
                    .sender(new UserSummary(userId, "SenderName"))
                    .receiver(new UserSummary(selectedUserId, "ReceiverName"))
                    .content("No messages yet")
                    .timestamp(null)
                    .build();

            messages = List.of(emptyConversation);
        }

        return ApiResponse.success(messages);
    }

    @GetMapping("/conversation-or-create/{userId}/{selectedUserId}")
    public ApiResponse<MessageResponse> getOrCreateConversation(
            @PathVariable Long userId,
            @PathVariable Long selectedUserId) {
        MessageResponse conversation = messageService.getOrCreateConversation(userId, selectedUserId);

        return ApiResponse.success(conversation);
    }




}
