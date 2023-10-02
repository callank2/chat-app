package com.kevin.chatapp.domain.usecase;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.exception.ChatException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;

public class UpdateChatTest {

    @Mock ChatRepository chatRepository;
    @Mock UserRepository userRepository;

    @InjectMocks UpdateChat updateChat;

    @BeforeEach
    void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateChat_invalidUsers_throwsException() {
        when(userRepository.findAllById(anyCollection())).thenReturn(Collections.EMPTY_LIST);

        Chat chat = new Chat(null, "text", List.of(UUID.randomUUID(), UUID.randomUUID()));

        ChatException chatException =
                assertThrows(ChatException.class, () -> updateChat.updateChat(chat));
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST.value(), chatException.getHttpStatusToRespond());
    }
}
