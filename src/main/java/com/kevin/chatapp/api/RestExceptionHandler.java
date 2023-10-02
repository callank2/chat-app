package com.kevin.chatapp.api;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mongodb.MongoClientException;

import com.kevin.chatapp.domain.exception.ChatException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ChatException.class)
    protected ResponseEntity<?> handleChatException(ChatException ex) {
        log.error("Application Error", ex);
        return new ResponseEntity<>(null, HttpStatus.valueOf(ex.getHttpStatusToRespond()));
    }

    @ExceptionHandler(MongoClientException.class)
    protected ResponseEntity<?> handleEntityValidation(MongoClientException ex) {
        log.error("Mongo Error", ex);
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
