package com.kevin.chatapp.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.usecase.ListChats;
import com.kevin.chatapp.domain.usecase.UpdateChat;

import static com.kevin.chatapp.JsonHelper.fromJsonString;
import static com.kevin.chatapp.JsonHelper.toJsonString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatAppApplication.class)
@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean ListChats listChats;
    @MockBean UpdateChat updateChat;

    @Test
    void updateChat_validBody_validResponse() throws Exception {
        String updateChatUrl = "/v1/chat";
        LocalDateTime now = LocalDateTime.now();
        Chat chat = new Chat(null, "text", List.of(UUID.randomUUID(), UUID.randomUUID()), now);
        Chat expectedChat =
                new Chat(
                        UUID.randomUUID(),
                        "text",
                        List.of(UUID.randomUUID(), UUID.randomUUID()),
                        now);
        when(updateChat.updateChat(chat)).thenReturn(expectedChat);

        MvcResult result =
                mockMvc.perform(
                                MockMvcRequestBuilders.post(updateChatUrl)
                                        .content(toJsonString(chat))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        String content = result.getResponse().getContentAsString();
        Chat returnedChat = fromJsonString(content, Chat.class);
        Assertions.assertEquals(expectedChat.id(), returnedChat.id());
        Assertions.assertEquals(expectedChat.name(), returnedChat.name());
        Assertions.assertEquals(expectedChat.userIds(), returnedChat.userIds());
    }

    @Test
    void listChats_validBody_validResponse() throws Exception {
        String listChatsUrl = "/v1/chats";
        LocalDateTime now = LocalDateTime.now();
        Chat expectedChat =
                new Chat(
                        UUID.randomUUID(),
                        "text",
                        List.of(UUID.randomUUID(), UUID.randomUUID()),
                        now);
        when(listChats.listChats(any())).thenReturn(List.of(expectedChat));

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.get(listChatsUrl))
                        .andExpect(status().isOk())
                        .andReturn();
        String content = result.getResponse().getContentAsString();
        List<Chat> returnedChats = fromJsonString(content, new TypeReference<List<Chat>>() {});
        Assertions.assertEquals(1, returnedChats.size());
        Assertions.assertEquals(expectedChat.id(), returnedChats.get(0).id());
        Assertions.assertEquals(expectedChat.name(), returnedChats.get(0).name());
        Assertions.assertEquals(expectedChat.userIds(), returnedChats.get(0).userIds());
    }
}
