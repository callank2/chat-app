package com.kevin.chatapp.domain;

import jakarta.validation.constraints.NotBlank;

public record Message(@NotBlank String text) {}
