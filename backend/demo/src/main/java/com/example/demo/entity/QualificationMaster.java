package com.example.demo.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "qualification_master")
//@Getter
//public class QualificationMaster {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    
//    @Column(name = "qualification_name")
//    private String qualificationName;
//    
//    @OneToMany(mappedBy = "qualificationMaster", cascade = CascadeType.ALL)
//    private List<Qualification> qualifications;
//}

@Getter
@Setter
@Entity
@Table(name = "qualification_master")
public class QualificationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "qualification_name")
    private String qualificationName;
}
