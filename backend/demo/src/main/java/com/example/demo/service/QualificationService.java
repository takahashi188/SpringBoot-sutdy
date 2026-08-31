package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.QualificationResponse;
import com.example.demo.entity.QualificationMaster;
import com.example.demo.repository.QualificationMasterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QualificationService {
	
	private final QualificationMasterRepository qualificationMasterRepository;
	
	public List<QualificationResponse> getQualificationMaster() {
		List<QualificationMaster> qualificationMasters = qualificationMasterRepository.findAll();
		
		List<QualificationResponse> qualificationResponses = qualificationMasters.stream().map(
				qualificationMaster -> {
				QualificationResponse qualificationResponse = new QualificationResponse();
				
				qualificationResponse.setQualificationId(qualificationMaster.getId());
				qualificationResponse.setQualificationName(qualificationMaster.getQualificationName());
				
				return qualificationResponse;
		}).toList();
		
		return qualificationResponses;
	}
}
