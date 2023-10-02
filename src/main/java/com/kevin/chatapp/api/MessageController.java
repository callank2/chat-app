package com.kevin.chatapp.api;

import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.exception.ChatException;
import com.kevin.chatapp.domain.usecase.CreateMessage;
import com.kevin.chatapp.domain.usecase.GetMessages;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class MessageController {

    private final CreateMessage createMessage;
    private final GetMessages getMessages;

    @PostMapping("/send")
    public Message sendMessage(@Valid @RequestBody Message message) throws ChatException {
        UUID userId = UUID.randomUUID(); // TODO - should come from header or session
        return createMessage.createMessage(userId, message);
    }

    @GetMapping("/messages")
    public List<Message> getChat(@RequestParam UUID chatId) throws ChatException {
        UUID userId = UUID.randomUUID(); // TODO - should come from header or session
        return getMessages.getMessages(userId, chatId);
    }
}
