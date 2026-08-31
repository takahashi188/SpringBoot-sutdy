package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.UserForm;

@Controller
public class UserFormController {

    @GetMapping("/create")
    public String UserCreate(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "userCreate";
    }
    
    @PostMapping("/result")
    public String UserResult(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userCreate";
        }
        
        return "userResult";
    }
}
