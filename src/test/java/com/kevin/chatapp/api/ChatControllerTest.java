package com.kevin.chatapp.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ChatControllerTest {

    private MockMvc mockMvc;

    private ChatController chatController;


    @BeforeEach
    void setUp() {
        chatController = new ChatController();
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }


    @Test
    void sendMessage() throws Exception {
        String sendMessageUrl = "/v1/send";
        mockMvc.perform(MockMvcRequestBuilders.post(sendMessageUrl)
                .content("message")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}