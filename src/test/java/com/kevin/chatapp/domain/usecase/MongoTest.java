package com.kevin.chatapp.domain.usecase;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class MongoTest {

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
}
