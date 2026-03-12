package com.example.demo.exception;

public class StockNotSufficientException extends RuntimeException {

    public StockNotSufficientException(String message) {
        super(message);
    }
}