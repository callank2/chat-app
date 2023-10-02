package com.kevin.chatapp.domain.usecase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

@Testcontainers
@SpringBootTest(classes = ChatAppApplication.class)
public class UpdateChatIT {

    @Container
    private static final GenericContainer MONGO_DB_CONTAINER =
            new GenericContainer(DockerImageName.parse("mongo:4.4.24"))
                    .withExposedPorts(27017)
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", "pooluser")
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", "pooluser");

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("mongodb.host", MONGO_DB_CONTAINER::getHost);
        registry.add("mongodb.port", () -> MONGO_DB_CONTAINER.getMappedPort(27017));
    }

    @BeforeAll
    public static void setupContainer() {
        MONGO_DB_CONTAINER.start();
    }

    @Autowired UpdateChat updateChat;
    @Autowired ChatRepository chatRepository;
    @Autowired UserRepository userRepository;

    List<User> validUsers;

    @BeforeEach
    public void setup() {
        User user1 =
                new User(UUID.randomUUID(), "testUser@gmail.com", "password", new ArrayList<>());
        validUsers = new ArrayList<>();
        validUsers.add(userRepository.save(user1));
    }

    @Test
    void updateChat_validChat_createsInDb() throws ChatException {

        Chat chat = new Chat(null, "text", List.of(validUsers.get(0).id(), UUID.randomUUID()));
        Chat updatedChat = updateChat.updateChat(chat);

        assertChatCreated(chat);

        assertUsersUpdated(updatedChat);
    }

    private void assertChatCreated(Chat chat) {
        Iterator<Chat> foundChats = chatRepository.findAll().iterator();
        int count = 0;
        while (foundChats.hasNext()) {
            count++;
            Chat foundChat = foundChats.next();
            Assertions.assertNotNull(foundChat.id());
            Assertions.assertEquals(chat.name(), foundChat.name());
            Assertions.assertEquals(List.of(validUsers.get(0).id()), foundChat.userIds());
        }
        Assertions.assertEquals(1, count);
    }

    private void assertUsersUpdated(Chat updatedChat) {
        Iterator<User> foundUsers = userRepository.findAll().iterator();
        int count = 0;
        while (foundUsers.hasNext()) {
            count++;
            User foundUser = foundUsers.next();
            Assertions.assertEquals(validUsers.get(0).id(), foundUser.id());
            Assertions.assertEquals(validUsers.get(0).username(), foundUser.username());
            Assertions.assertEquals(validUsers.get(0).password(), foundUser.password());
            Assertions.assertEquals(List.of(updatedChat.id()), foundUser.chatIds());
        }
        Assertions.assertEquals(1, count);
    }
}
