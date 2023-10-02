package com.kevin.chatapp.api;

import java.util.UUID;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.exception.ChatException;
import com.kevin.chatapp.domain.usecase.CreateMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class MessageController {

    private final CreateMessage createMessage;

    @PostMapping("/send")
    public Message sendMessage(@Valid @RequestBody Message message) throws ChatException {
        UUID userId = UUID.randomUUID(); // TODO - should come from header or session
        return createMessage.createMessage(userId, message);
    }
}
