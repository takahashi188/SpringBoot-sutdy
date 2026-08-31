package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    
//    public User(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//    
//    public int getId() {
//        return this.id;
//    }
//    
//    public String getName() {
//        return this.name;
//    }
}
