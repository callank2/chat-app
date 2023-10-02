package com.kevin.chatapp.domain.exception;

import lombok.Getter;

@Getter
public class ChatException extends Exception {
    private final int httpStatusToRespond;

    public ChatException(String msg, int httpStatusToRespond) {
        super(msg);
        this.httpStatusToRespond = httpStatusToRespond;
    }
}
