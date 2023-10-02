package com.kevin.chatapp.api;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.exception.ChatException;
import com.kevin.chatapp.domain.usecase.UpdateChat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class ChatController {

    private final UpdateChat updateChat;

    @PostMapping("/chat")
    public Chat createChat(@Valid @RequestBody Chat chat) throws ChatException {
        return updateChat.updateChat(chat);
    }
}
