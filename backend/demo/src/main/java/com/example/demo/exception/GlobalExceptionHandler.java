package com.example.demo.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.ExistErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExistErrorResponse> handleMethodArgumentNotValidException(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = ex.getBindingResult()
	            .getFieldErrors()
	            .stream()
	            .collect(Collectors.toMap(
	                    error -> error.getField(),
	                    error -> error.getDefaultMessage(),
	                    (message1, message2) -> message1
	            ));

	    ExistErrorResponse validErrorResponse =
	            new ExistErrorResponse(errors);

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(validErrorResponse);
	}
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ExistErrorResponse> handleEmailAlreadyExsist(
            EmailAlreadyExistsException e) {
        ExistErrorResponse existErrorResponse = new ExistErrorResponse(
                Map.of("email", e.getMessage()
                        )
                );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(existErrorResponse);
    }
    
    @ExceptionHandler(InvalidPageException.class)
    public ResponseEntity<ExistErrorResponse> handleValidPage(InvalidPageException e) {
        ExistErrorResponse validErrorResponse = new ExistErrorResponse(
                Map.of(e.getKey(), e.getMessage()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validErrorResponse);
    }
    
    @ExceptionHandler(InvalidQualificationException.class)
    public ResponseEntity<ExistErrorResponse> handleValidQualification(InvalidQualificationException e) {
        ExistErrorResponse validErrorResponse = new ExistErrorResponse(
                Map.of("qualificationId", e.getMessage()));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validErrorResponse);
    }
}