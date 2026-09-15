package com.example.demo.entity;

import java.time.LocalDate;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Table(name = "user_qualifications")
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Qualification {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    
//    @Setter
//    @ManyToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User user;
//    
//    @Setter
//    @ManyToOne
//    @JoinColumn(name = "qualification_id", referencedColumnName = "id", nullable = false)
//    private QualificationMaster qualificationMaster;
//    
//    @Setter
//    private LocalDate acquisitionDate;
//}

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_qualifications")
public class Qualification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "qualification_id")
    private Integer qualificationId;
    
    @Column(name = "acquisition_date")
    private LocalDate acquisitionDate;
}
