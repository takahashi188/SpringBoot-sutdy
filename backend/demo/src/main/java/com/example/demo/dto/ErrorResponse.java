package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> fieldErrors;
    
    @Getter
    @Setter
    @AllArgsConstructor
    public static class FieldError {
    private String field;
    private Object rejectedValue;
    private String message;
    }
}
