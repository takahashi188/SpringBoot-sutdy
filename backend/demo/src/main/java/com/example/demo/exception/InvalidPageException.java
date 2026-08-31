package com.example.demo.exception;

import lombok.Getter;

public class InvalidPageException extends RuntimeException {

    public InvalidPageException(String key, String message) {
        super(message);
        this.key = key;
    }
    
    @Getter
    private String key;
}
