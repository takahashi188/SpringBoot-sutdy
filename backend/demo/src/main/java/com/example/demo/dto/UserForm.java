package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {
    @NotBlank(message = "名前は必須です")
    @Size(max = 20, message = "名前は20文字以内で入力してください")
    private String name;
    
    @Min(value = 0, message = "年齢は0以上を入力してください")
    @Max(value = 120, message = "年齢は120以下を入力してください")
    private Integer age;
}
