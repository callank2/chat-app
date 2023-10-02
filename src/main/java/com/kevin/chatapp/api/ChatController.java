package com.kevin.chatapp.api;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.exception.ChatException;
import com.kevin.chatapp.domain.usecase.ListChats;
import com.kevin.chatapp.domain.usecase.UpdateChat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class ChatController {

    private final ListChats listChats;
    private final UpdateChat updateChat;

    @PostMapping("/chat")
    public Chat createChat(@Valid @RequestBody Chat chat) throws ChatException {
        return updateChat.updateChat(chat);
    }

    @GetMapping("/chats")
    public List<Chat> listChats() throws ChatException {
        UUID userId = UUID.randomUUID(); // TODO - should be provided by session
        return listChats.listChats(userId);
    }
}
