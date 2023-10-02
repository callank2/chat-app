package com.kevin.chatapp.config;

import org.bson.UuidRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.Setter;

@Setter
@Configuration
@ConfigurationProperties(prefix = "mongodb")
@EnableMongoRepositories("com.kevin.chatapp.data")
public class MongoConfig {

    private String host;
    private Integer port;
    private String authenticationDatabase;
    private String username;
    private String password;
    private String database;

    @Bean
    public MongoClient mongo() {
        String uri = String.format("mongodb://%s:%s@%s:%s/", username, password, host, port);
        ConnectionString connectionString = new ConnectionString(uri);
        MongoCredential credential =
                MongoCredential.createCredential(
                        username, authenticationDatabase, password.toCharArray());
        return MongoClients.create(
                MongoClientSettings.builder()
                        .credential(credential)
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .applyConnectionString(connectionString)
                        .build());
    }
}
