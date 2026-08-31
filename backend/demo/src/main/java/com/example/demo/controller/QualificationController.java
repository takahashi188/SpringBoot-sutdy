package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.QualificationResponse;
import com.example.demo.service.QualificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class QualificationController {
	private final QualificationService qualificationService;
	
	@GetMapping("/api/qualification-master")
	public ResponseEntity<List<QualificationResponse>> findAll() {
		List<QualificationResponse> qualificationResponses = qualificationService.getQualificationMaster();
		return ResponseEntity.ok(qualificationResponses);
	}
}
