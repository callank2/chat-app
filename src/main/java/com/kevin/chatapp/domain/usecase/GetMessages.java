package com.kevin.chatapp.domain.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kevin.chatapp.data.MessageRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMessages {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Message> getMessages(UUID userId, UUID chatId) throws ChatException {

        validateUserAccess(userId, chatId);

        List<Message> messages = messageRepository.findByChatId(chatId);

        return messages;
    }

    /**
     * This request validates the user can actually send a message to the requested chat. Currently
     * implemented in same DB. Could be handled by a permissions/access service.
     *
     * @param userId - user sending the message
     * @param chatId - message destination
     * @throws ChatException
     */
    private void validateUserAccess(UUID userId, UUID chatId) throws ChatException {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () ->
                                        new ChatException(
                                                "Invalid user",
                                                HttpStatus.BAD_REQUEST
                                                        .value())); // user is logged in so should
        // never happen
        if (!user.chatIds().contains(chatId))
            throw new ChatException(
                    String.format("User %s can't access chat %s", userId, chatId),
                    HttpStatus.UNAUTHORIZED.value());
    }
}
