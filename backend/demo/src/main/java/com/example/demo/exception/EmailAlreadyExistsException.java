package com.example.demo.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("既に同じメールアドレスが登録されています");
    }

}