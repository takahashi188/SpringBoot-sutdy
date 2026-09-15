package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.UserDao;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserInfoResponse;
import com.example.demo.entity.User;
import com.example.demo.service.JwtService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


//	private final UserRepository userRepository;
	private final UserDao userDao;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

//	@GetMapping("/me")
//	public ResponseEntity<LoginUserResponse> me(Authentication authentication) {
//
//		User user =
//				userRepository.findByEmail(authentication.getName())
//				.orElseThrow();
//
//		return ResponseEntity.ok(new  LoginUserResponse(
//				user.getId(),
//				user.getName(),
//				user.getEmail()
//				));
//	}
	
	@GetMapping("/me")
	public UserInfoResponse me(
	        Authentication authentication) {

	    User user =
	            userDao.findByEmail(
	                    authentication.getName()).orElseThrow();

	    return new UserInfoResponse(
	    		user.getId(),
	            user.getName(),
	            user.getEmail());
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
		// 入力されたメールアドレスとパスワードで認証を実行
		// 認証に失敗した場合は AuthenticationException が発生する
		try {
		authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(
		                loginRequest.getEmail(),
		                loginRequest.getPassword()));

		// 認証成功後、メールアドレスを元にJWTを生成
		String token =
		        jwtService.generateToken(loginRequest.getEmail());
		
		return ResponseEntity.ok(new LoginResponse(token));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}