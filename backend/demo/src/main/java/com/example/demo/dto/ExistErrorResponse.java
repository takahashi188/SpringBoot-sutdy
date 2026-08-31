package com.example.demo.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExistErrorResponse {

    private Map<String, String> errors;
}