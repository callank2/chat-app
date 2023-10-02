package com.kevin.chatapp.domain.usecase;

import java.util.ArrayList;
import java.util.Collection;
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
public class UpdateChat {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public Chat updateChat(Chat request) throws ChatException {
        UUID id = idForUpsert(request);

        List<UUID> userIds = getUsersForChat(request.userIds());
        if (userIds.isEmpty())
            throw new ChatException("No users found for Chat", HttpStatus.BAD_REQUEST.value());

        Chat chatToUpdate = new Chat(id, request.name(), userIds);
        Chat savedChat = chatRepository.save(chatToUpdate);

        updateUserAccess(savedChat.id(), userIds);

        return savedChat;
    }

    private UUID idForUpsert(Chat request) {
        if (request.id() != null) return request.id();
        return UUID.randomUUID();
    }

    /**
     * This method makes sure all the targeted users exist and are available for access. This is a
     * WIP. Using a Mongo collection to represent users, but ideally would have a permissions/access
     * service.
     *
     * @param userIds - userIds to validate existence of
     * @return
     */
    private List<UUID> getUsersForChat(List<UUID> userIds) {
        Iterable<User> users = userRepository.findAllById(userIds);
        List<UUID> existingUserIds = new ArrayList<>();
        for (User user : users) {
            existingUserIds.add(user.id());
        }
        Collection<UUID> nonExistingUuids = new ArrayList<>(userIds);
        nonExistingUuids.removeAll(existingUserIds);
        if (!nonExistingUuids.isEmpty())
            log.error("The users {} are not available for targeting", nonExistingUuids);
        return existingUserIds;
    }

    /**
     * This method updates user access to the updated chat. This is WIP. Ideally a
     * permissions/access service handles this. For now this only handles adding users to the chat
     * and not removing users from the chat.
     *
     * @param chatId - id of the chat where access should be updated
     * @param userIds - ids of the users that can now see the chat
     */
    private void updateUserAccess(UUID chatId, List<UUID> userIds) {
        Iterable<User> users = userRepository.findAllById(userIds);
        List<User> usersToUpdate = new ArrayList<>();
        for (User user : users) {
            if (user.chatIds().contains(chatId)) continue;
            List<UUID> chatIds = new ArrayList<>(user.chatIds());
            chatIds.add(chatId);
            User userToUpdate = new User(user.id(), user.username(), user.password(), chatIds);
            usersToUpdate.add(userToUpdate);
        }
        userRepository.saveAll(usersToUpdate);
    }
}
