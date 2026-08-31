package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileCreateRequest {

    private String nickname;
    
    private LocalDate birthday;
}
