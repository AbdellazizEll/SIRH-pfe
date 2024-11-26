package com.example.anywrpfe.auth.exception;

public class PasswordMismatchException extends RuntimeException{
    public PasswordMismatchException(String message) {
        super(message);
    }
}
