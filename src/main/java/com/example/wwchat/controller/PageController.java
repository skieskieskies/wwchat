package com.example.wwchat.controller;

import com.example.wwchat.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/chat")
    public String redirectToChatPage() {
        return "redirect:/chat.html";
    }
}