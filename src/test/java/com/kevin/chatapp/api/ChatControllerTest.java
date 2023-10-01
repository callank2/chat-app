package com.kevin.chatapp.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.domain.Chat;

import static com.kevin.chatapp.JsonHelper.fromJsonString;
import static com.kevin.chatapp.JsonHelper.toJsonString;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatAppApplication.class)
@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void sendMessage_validBody_validResponse() throws Exception {
        String sendMessageUrl = "/v1/chat";
        Chat chat = new Chat(null, "text");

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
        Assertions.assertNotNull(returnedChat.chatId());
        Assertions.assertEquals(chat.name(), returnedChat.name());
    }
}
