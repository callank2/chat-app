package com.kevin.chatapp.domain;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record Chat(
        UUID chatId,
        @NotBlank String name) {}
