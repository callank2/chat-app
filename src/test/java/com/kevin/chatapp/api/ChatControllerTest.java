package com.kevin.chatapp.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.domain.Message;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatAppApplication.class)
@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void sendMessage_validBody_validResponse() throws Exception {
        String sendMessageUrl = "/v1/send";
        Message message = new Message("text");
        mockMvc.perform(
                        MockMvcRequestBuilders.post(sendMessageUrl)
                                .content(toJsonString(message))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(message.text()));
    }

    @Test
    void sendMessage_emptyBody_badRequestResponse() throws Exception {
        String sendMessageUrl = "/v1/send";
        Message message = new Message("");
        mockMvc.perform(
                        MockMvcRequestBuilders.post(sendMessageUrl)
                                .content(toJsonString(message))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message.text()));
    }

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
