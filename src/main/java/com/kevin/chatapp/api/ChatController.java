package com.kevin.chatapp.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Chat;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1")
public class ChatController {

    @PostMapping("/chat")
    public String sendMessage(@Valid @RequestBody Chat chat) {
        return chat.name();
    }
}