package com.kevin.chatapp.data;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.kevin.chatapp.domain.Chat;

public interface ChatRepository extends CrudRepository<Chat, UUID> {}
