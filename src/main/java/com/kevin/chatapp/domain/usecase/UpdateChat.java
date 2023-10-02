package com.kevin.chatapp.domain.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.domain.Chat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UpdateChat {

    private final ChatRepository chatRepository;

    public Chat updateChat(Chat request) {

        Chat chatToUpdate = prepareForUpsert(request);

        // Validate all users defined for chat exist
        // add chat Id to all User records, so they can access

        // Create a chat in DB
        return chatRepository.save(chatToUpdate);
    }

    private Chat prepareForUpsert(Chat request) {
        if (request.id() != null) return request;
        return new Chat(UUID.randomUUID(), request.name(), request.userIds());
    }
}
