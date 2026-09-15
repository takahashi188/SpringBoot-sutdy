package com.example.demo.entity;

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

// このEntityでAuditing機能を有効化
//EntityListeners(AuditingEntityListener.class)
//@Entity
//@Table(name = "users")
//@Getter
//@NoArgsConstructor
//@SQLDelete(sql = "update users set deleted = true where id = ?")
//@SQLRestriction("deleted = false")
//public class User {
//    public User(String name, String email, String password) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    @Id@
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    
//    @Setter
//    private String name;
//    
//    @Setter
//    @Column(nullable = false, unique = true)
//    private String email;
//    
//    @Setter
//    @Column(nullable = false)
//    private String password;
//    
//    @CreatedDate
//    // update時に更新しない
//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//    
//    @LastModifiedDate
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//    
//    @Column(columnDefinition = "boolean default false")
//    private boolean deleted;
//    
//    @Setter
//    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    private Profile profile;
//    
//    @Setter
//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
//    private List<Qualification> qualifications;
//}

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String email;

	private String password;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private boolean deleted;

}
