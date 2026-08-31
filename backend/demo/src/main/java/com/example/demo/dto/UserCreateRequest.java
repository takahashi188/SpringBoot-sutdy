package com.example.demo.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank(message = "名前は必須です")
    private String name;
    
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレス形式で入力してください")
    private String email;
    
    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上にしてください")
    private String password;
    
    private ProfileCreateRequest profile;
    
    private List<QualificationRequest> qualifications;
}
