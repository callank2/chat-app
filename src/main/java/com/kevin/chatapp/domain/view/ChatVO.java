package com.kevin.chatapp.domain.view;

import java.util.UUID;

public record ChatVO(
        UUID id, String name, String thumbnailUrl, String lastPostMsg, String lastPostDateMsg) {}
