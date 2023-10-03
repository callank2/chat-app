package com.kevin.chatapp.domain.view;

import java.util.List;
import java.util.UUID;

/**
 * Groups messages from the same author together, to simplify generating HTML for the Chat view
 */
public record MessageGroupVO(
      String thumbnailUrl,
      UUID userId,
      boolean isLoggedInUser,
      List<MessageVO> messages
){}
