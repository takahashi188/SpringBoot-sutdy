package com.example.demo.dto;

import java.time.LocalDate;

import org.seasar.doma.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(immutable = false)
@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
	private String nickname;
	
	private LocalDate birthday;
	
	private Integer qualificationId;
	
	private LocalDate acquisitionDate;
	
	private String qualificationName;
}
