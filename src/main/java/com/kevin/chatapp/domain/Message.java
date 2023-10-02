package com.kevin.chatapp.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;

@Builder
@Document("message")
public record Message(
        UUID id,
        @NotBlank String text,
        @NotNull UUID chatId,
        LocalDateTime datePosted,
        UUID author) {}
