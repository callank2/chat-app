package com.kevin.chatapp.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.chatapp.domain.Message;

@RestController
@RequestMapping("/v1")
public class ChatController {

    @PostMapping("/send")
    public String sendMessage(@RequestBody Message message) {
        return message.text();
    }
}
