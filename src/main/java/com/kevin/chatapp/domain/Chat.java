package com.kevin.chatapp.domain;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat")
public record Chat(UUID id, @NotBlank String name, @NotEmpty List<UUID> userIds) {}
