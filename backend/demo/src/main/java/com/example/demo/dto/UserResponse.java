package com.example.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    public UserResponse(Integer id, String name, String email, String message) {
        this(id, name, email, null, null, message);
    }
    
    private Integer id;
    private String name;
    private String email;
    private ProfileResponse profile;
    private List<QualificationResponse> qualifications;
    private String message;
}
