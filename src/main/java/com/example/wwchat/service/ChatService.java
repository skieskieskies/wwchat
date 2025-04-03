package com.example.wwchat.service;

import com.example.wwchat.data.model.Message;
import com.example.wwchat.data.repository.MessageRepository;
import com.example.wwchat.security.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;

    public List<Message> getLastMessages() {
        return messageRepository.findTop100ByOrderByTimestampDesc();
    }

    public Message saveMessage(Message message, String author) {
        message.setAuthor(author);
       return messageRepository.save(message);
    }
}