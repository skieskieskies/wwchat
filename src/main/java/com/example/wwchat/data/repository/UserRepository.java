package com.example.wwchat.data.repository;

import com.example.wwchat.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByOnlineTrue();
    Optional<User> findById(String id);

    Optional<User> findByNickname(String name);
}
