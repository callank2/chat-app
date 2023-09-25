package com.kevin.chatapp.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ChatController {

    @PostMapping("/send")
    public boolean sendMessage(String message){
        return true;
    }
}
