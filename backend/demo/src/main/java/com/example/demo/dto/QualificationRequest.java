package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QualificationRequest {
    
    private Integer qualificationId;
    
    private LocalDate acquisitionDate;
}
