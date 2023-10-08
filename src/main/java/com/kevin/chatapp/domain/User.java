package com.kevin.chatapp.domain;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;

@Builder(toBuilder = true)
@Document("user")
public record User(
        UUID id,
        @NotBlank String username,
        @NotBlank String password,
        List<UUID> chatIds) {}
