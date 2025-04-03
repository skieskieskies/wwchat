package com.example.wwchat.controller;

import com.example.wwchat.data.model.Message;
import com.example.wwchat.data.model.User;
import com.example.wwchat.data.repository.UserRepository;
import com.example.wwchat.security.CustomOAuth2User;
import com.example.wwchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserRepository userRepository;

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message, @AuthenticationPrincipal Authentication user) {
        return chatService.saveMessage(message, ((CustomOAuth2User)user.getPrincipal()).getName());
    }

    @GetMapping("/")
    public String helloPage() {
        return "Это чат";
    }

    @GetMapping("/api/messages")
    public List<Message> getMessages() {
        return chatService.getLastMessages();
    }

    @MessageMapping("/request-users")
    public void countUsers() {
        List<User> onlineUsers = userRepository.findByOnlineTrue();
        messagingTemplate.convertAndSend("/topic/users", onlineUsers);
    }
}