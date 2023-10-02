package com.kevin.chatapp.domain.usecase;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.data.MessageRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

@SpringBootTest(classes = ChatAppApplication.class)
public class CreateMessageIT extends MongoTest {

    @Autowired CreateMessage createMessage;

    @Autowired ChatRepository chatRepository;
    @Autowired MessageRepository messageRepository;
    @Autowired UserRepository userRepository;

    User validUser;
    Chat validChat;

    @BeforeEach
    public void setup() {
        UUID chatId = UUID.randomUUID();
        User user1 = new User(UUID.randomUUID(), "testUser@gmail.com", "password", List.of(chatId));
        validUser = userRepository.save(user1);

        Chat chat = new Chat(chatId, "Chat 1", List.of(validUser.id()), LocalDateTime.now());
        validChat = chatRepository.save(chat);
    }

    @Test
    void createMessage_validRequest_saveInDb() throws ChatException {
        LocalDateTime testDate = LocalDateTime.now();
        UUID userId = validUser.id();
        Message message = Message.builder().text("text").chatId(validChat.id()).build();

        createMessage.createMessage(userId, message);

        assertMessageCreated(message, userId, testDate);
    }

    private void assertMessageCreated(Message message, UUID author, LocalDateTime testDate) {
        Iterator<Message> foundMsgs = messageRepository.findAll().iterator();
        int count = 0;
        while (foundMsgs.hasNext()) {
            count++;
            Message foundMsg = foundMsgs.next();
            Assertions.assertNotNull(foundMsg.id());
            Assertions.assertEquals(message.text(), foundMsg.text());
            Assertions.assertEquals(message.chatId(), foundMsg.chatId());
            Assertions.assertEquals(author, foundMsg.author());
            Assertions.assertTrue(LocalDateTime.now().isAfter(foundMsg.datePosted()));
            Assertions.assertTrue(testDate.isBefore(foundMsg.datePosted()));
        }
        Assertions.assertEquals(1, count);
    }
}
