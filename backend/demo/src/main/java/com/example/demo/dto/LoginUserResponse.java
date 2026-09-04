package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserResponse {
	
	private Integer id;
	
	private String name;
	
	private String email;

}
