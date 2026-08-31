package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QualificationResponse {
    
    private Integer qualificationId;
    
    private String qualificationName;
    
    private LocalDate acquisitionDate;
}
