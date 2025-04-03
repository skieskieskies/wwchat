package com.example.wwchat.listener;

import com.example.wwchat.data.model.User;
import com.example.wwchat.data.repository.UserRepository;
import com.example.wwchat.security.CustomOAuth2User;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Optional;

@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate, UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    @EventListener
    public void handleWebSocketConnect(SessionConnectedEvent event) {
        sendOnlineUsers();
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        var principal = event.getUser().getName();
        var user = userRepository.findByNickname(principal).orElseThrow(IllegalStateException::new);
        user.setOnline(false);
        userRepository.save(user);
        sendOnlineUsers();
    }

    private void sendOnlineUsers() {
        List<User> onlineUsers = userRepository.findByOnlineTrue();
        messagingTemplate.convertAndSend("/topic/users", onlineUsers);
    }
}