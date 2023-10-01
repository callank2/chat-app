package com.kevin.chatapp.api;

import java.util.UUID;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Chat;

@RestController
@RequestMapping("/v1")
public class ChatController {

    @PostMapping("/chat")
    public Chat createChat(@Valid @RequestBody Chat chat) {
        return new Chat(UUID.randomUUID(), chat.name());
    }
}
