package com.endava.school.supermarketapi.exception;

public class MoneyAreNotEnoughException extends RuntimeException{
    public MoneyAreNotEnoughException(String message) {
        super(message);
    }
}
