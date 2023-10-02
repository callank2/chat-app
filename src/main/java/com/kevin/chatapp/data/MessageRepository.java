package com.kevin.chatapp.data;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.kevin.chatapp.domain.Message;

public interface MessageRepository extends CrudRepository<Message, UUID> {

    List<Message> findByChatId(UUID chatId);
}
