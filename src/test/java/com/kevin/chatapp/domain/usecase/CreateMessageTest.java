package com.kevin.chatapp.domain.usecase;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.kevin.chatapp.data.MessageRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.exception.ChatException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreateMessageTest {

    @Mock MessageRepository messageRepository;
    @Mock UserRepository userRepository;

    @InjectMocks CreateMessage createMessage;

    @BeforeEach
    void openMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMessage_invalidUser_throwsException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Message message = Message.builder().text("text").chatId(UUID.randomUUID()).build();

        ChatException chatException =
                assertThrows(
                        ChatException.class,
                        () -> createMessage.createMessage(UUID.randomUUID(), message));
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST.value(), chatException.getHttpStatusToRespond());
    }

    @Test
    void createMessage_userUnauthorized_throwsException() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        Message message = Message.builder().text("text").chatId(UUID.randomUUID()).build();

        ChatException chatException =
                assertThrows(
                        ChatException.class,
                        () -> createMessage.createMessage(UUID.randomUUID(), message));
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST.value(), chatException.getHttpStatusToRespond());
    }
}
