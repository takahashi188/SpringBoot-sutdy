package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//@EntityListeners(AuditingEntityListener.class)
//@Entity
//@Table(name = "profiles")
//@Getter
//@NoArgsConstructor
//public class Profile {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    
//    @Setter
//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User user;
//    
//    @Setter
//    @Column(nullable = true)
//    private String nickname;
//    
//    @Setter
//    @Column(nullable = true)
//    private LocalDate birthday;
//    
//    @CreatedDate
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//    
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//}

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;
    
    private String nickname;
    
    private LocalDate birthday;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}