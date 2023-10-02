package com.kevin.chatapp.domain.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ChatAppApplication.class)
public class ListChatsIT extends MongoTest {

    @Autowired ListChats listChats;
    @Autowired ChatRepository chatRepository;
    @Autowired UserRepository userRepository;

    User validUser;
    List<Chat> createdChats;

    @BeforeEach
    public void setup() {
        User user1 =
                new User(UUID.randomUUID(), "testUser@gmail.com", "password", new ArrayList<>());
        validUser = userRepository.save(user1);

        createdChats = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Chat chat =
                    new Chat(
                            UUID.randomUUID(),
                            "text",
                            List.of(validUser.id()),
                            LocalDateTime.now());
            chat = chatRepository.save(chat);
            createdChats.add(chat);
        }

        validUser =
                validUser.toBuilder()
                        .chatIds(createdChats.stream().map(Chat::id).collect(Collectors.toList()))
                        .build();
        validUser = userRepository.save(validUser);
    }

    @Test
    void listChats_validUser_returnsList() throws ChatException {

        List<Chat> foundChats = listChats.listChats(validUser.id());

        assertEquals(createdChats.size(), foundChats.size());
        createdChats.stream()
                .map(Chat::id)
                .forEach(
                        expected -> {
                            Assertions.assertTrue(
                                    foundChats.stream()
                                            .anyMatch(found -> found.id().equals(expected)));
                        });
        // TODO - the chats are not yet returned sorted by last post time
    }
}
