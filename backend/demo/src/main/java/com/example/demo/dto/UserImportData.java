package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Profile;
import com.example.demo.entity.Qualification;
import com.example.demo.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserImportData {

	private User user;
	
	private Profile profile;
	
	private List<Qualification> qualifications;
}
