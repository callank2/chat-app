package com.kevin.chatapp.domain.usecase;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import com.kevin.chatapp.domain.Chat;

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

    @Test
    void updateChat_validChat_createsInDb() {

        Chat chat = new Chat(null, "text", List.of(UUID.randomUUID(), UUID.randomUUID()));
        updateChat.updateChat(chat);

        Iterator<Chat> foundChats = chatRepository.findAll().iterator();
        int count = 0;
        while (foundChats.hasNext()) {
            count++;
            Chat foundChat = foundChats.next();
            Assertions.assertNotNull(foundChat.id());
            Assertions.assertEquals(chat.name(), foundChat.name());
            Assertions.assertEquals(chat.userIds(), foundChat.userIds());
        }
        Assertions.assertEquals(1, count);
    }
}
