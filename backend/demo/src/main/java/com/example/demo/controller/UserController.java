package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userRequest) {
        UserResponse newUser = userService.create(userRequest);
        return ResponseEntity.ok(newUser);
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Integer id) {
        UserResponse updatedUser = userService.update(userUpdateRequest, id);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Integer id) {
        UserResponse deletedUser = userService.delete(id);
        return ResponseEntity.ok(deletedUser);
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<Page<UserResponse>> findUserByName(@PageableDefault(page = 0, size = 10) Pageable pageable, @RequestParam String name) {
        Page<UserResponse> findedUsers = userService.findByName(name, pageable);
        return ResponseEntity.ok(findedUsers);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Integer id) {
        UserResponse findedUser = userService.findById(id);
        return ResponseEntity.ok(findedUser);
    }
    
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> findUserAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<UserResponse> findedUsers = userService.findAll(pageable);
        return ResponseEntity.ok(findedUsers);
    }
    
}
