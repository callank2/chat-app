package com.kevin.chatapp.domain;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record Chat(UUID chatId, @NotBlank String name, @NotEmpty List<UUID> userIds) {}
