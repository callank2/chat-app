package com.kevin.chatapp.data;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.kevin.chatapp.domain.User;

public interface UserRepository extends CrudRepository<User, UUID> {}
