package com.kevin.chatapp.api;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.kevin.chatapp.config.ChatAppApplication;
import com.kevin.chatapp.domain.Message;
import com.kevin.chatapp.domain.usecase.CreateMessage;

import static com.kevin.chatapp.JsonHelper.toJsonString;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ChatAppApplication.class)
@WebMvcTest(controllers = MessageController.class)
class MessageControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CreateMessage createMessage;

    @Test
    void sendMessage_validBody_validResponse() throws Exception {
        String sendMessageUrl = "/v1/send";
        Message message = new Message(null, "text", UUID.randomUUID(), null, null);
        when(createMessage.createMessage(any(), any())).thenReturn(message);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(sendMessageUrl)
                                .content(toJsonString(message))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(toJsonString(message)));
    }

    @Test
    void sendMessage_emptyBody_badRequestResponse() throws Exception {
        String sendMessageUrl = "/v1/send";
        Message message = new Message(null, "", UUID.randomUUID(), null, null);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(sendMessageUrl)
                                .content(toJsonString(message))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
