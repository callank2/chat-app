package com.kevin.chatapp.domain.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kevin.chatapp.data.ChatRepository;
import com.kevin.chatapp.data.UserRepository;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.User;
import com.kevin.chatapp.domain.exception.ChatException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListChats {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public List<Chat> listChats(UUID userId) throws ChatException {

        List<UUID> chatIds = getChatIdsWithUserAccess(userId);

        List<Chat> chats = new ArrayList<>();
        chatRepository.findAllById(chatIds).forEach(chats::add);

        return chats;
    }

    /**
     * This request gets a list of chats user can access. Currently implemented in same DB. Could be
     * handled by a permissions/access service.
     *
     * @param userId - user sending the message
     * @throws ChatException
     */
    private List<UUID> getChatIdsWithUserAccess(UUID userId) throws ChatException {
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow( // user is logged in so should never happen
                                () ->
                                        new ChatException(
                                                "Invalid user", HttpStatus.BAD_REQUEST.value()));
        return user.chatIds();
    }
}
