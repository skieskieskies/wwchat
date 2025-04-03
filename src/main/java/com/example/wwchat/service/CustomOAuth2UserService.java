package com.example.wwchat.service;

import com.example.wwchat.data.model.User;
import com.example.wwchat.data.repository.UserRepository;
import com.example.wwchat.security.CustomOAuth2User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oauthUser.getAttributes();

        String userId = (String) attributes.get("sub");
        String name = (String) attributes.get("name");

        User user = userRepository.findById(userId)
                .orElseGet(() -> User.builder()
                        .id(userId)
                        .nickname(name)
                        .online(true)
                        .build());

        user.setOnline(true);
        userRepository.save(user);
        messagingTemplate.convertAndSend("/topic/users", userRepository.findByOnlineTrue());
        return new CustomOAuth2User(attributes);
    }

}
