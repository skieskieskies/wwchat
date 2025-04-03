package com.example.wwchat.data.repository;

import com.example.wwchat.data.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop100ByOrderByTimestampDesc();


}