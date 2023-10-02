package com.kevin.chatapp.api;

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

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.domain.Chat;
import com.kevin.chatapp.domain.usecase.UpdateChat;

import static com.kevin.chatapp.JsonHelper.fromJsonString;
import static com.kevin.chatapp.JsonHelper.toJsonString;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatAppApplication.class)
@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean UpdateChat updateChat;

    @Test
    void sendMessage_validBody_validResponse() throws Exception {
        String sendMessageUrl = "/v1/chat";
        Chat chat = new Chat(null, "text", List.of(UUID.randomUUID(), UUID.randomUUID()));
        Chat expectedChat =
                new Chat(UUID.randomUUID(), "text", List.of(UUID.randomUUID(), UUID.randomUUID()));
        when(updateChat.updateChat(chat)).thenReturn(expectedChat);

        MvcResult result =
                mockMvc.perform(
                                MockMvcRequestBuilders.post(sendMessageUrl)
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
}
