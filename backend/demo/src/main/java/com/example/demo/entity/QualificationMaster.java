package com.example.demo.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "qualification_master")
@Getter
public class QualificationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "qualification_name")
    private String qualificationName;
    
    @OneToMany(mappedBy = "qualificationMaster", cascade = CascadeType.ALL)
    private List<Qualification> qualifications;
}
