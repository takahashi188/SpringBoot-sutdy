package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginUserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository userRepository;

	@GetMapping("/me")
	public ResponseEntity<LoginUserResponse> me(Authentication authentication) {

	    User user =
	        userRepository.findByEmail(authentication.getName())
	            .orElseThrow();

	    return ResponseEntity.ok(new  LoginUserResponse(
	        user.getName(),
	        user.getEmail()
	    ));
	}
}