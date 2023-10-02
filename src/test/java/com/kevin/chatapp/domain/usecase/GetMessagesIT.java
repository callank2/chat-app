package com.kevin.chatapp.domain.usecase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.data.MessageRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ChatAppApplication.class)
public class GetMessagesIT extends MongoTest {

    @Autowired GetMessages getMessages;
    @Autowired MessageRepository messageRepository;
    @Autowired UserRepository userRepository;

    UUID chatId;
    User validUser;
    List<Message> createdMessages;

    @BeforeEach
    public void setup() {
        chatId = UUID.randomUUID();
        User user1 = new User(UUID.randomUUID(), "testUser@gmail.com", "password", List.of(chatId));
        validUser = userRepository.save(user1);

        createdMessages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Message message =
                    Message.builder()
                            .id(UUID.randomUUID())
                            .text("text")
                            .chatId(chatId)
                            .datePosted(LocalDateTime.now())
                            .author(validUser.id())
                            .build();

            message = messageRepository.save(message);
            createdMessages.add(message);
        }
    }

    @Test
    void listChats_validUser_returnsList() throws ChatException {

        List<Message> foundMsgs = getMessages.getMessages(validUser.id(), chatId);

        assertEquals(createdMessages.size(), foundMsgs.size());
        createdMessages.stream()
                .map(Message::id)
                .forEach(
                        expected -> {
                            Assertions.assertTrue(
                                    foundMsgs.stream()
                                            .anyMatch(found -> found.id().equals(expected)));
                        });
        // TODO - the messages are not yet returned sorted by last post time
    }
}
