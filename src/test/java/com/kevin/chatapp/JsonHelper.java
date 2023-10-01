package com.kevin.chatapp;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
