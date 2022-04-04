package com.endava.school.supermarketapi.exception;

public class SupermarketNotFoundException extends RuntimeException{
    public SupermarketNotFoundException(String message) {
        super(message);
    }
}
