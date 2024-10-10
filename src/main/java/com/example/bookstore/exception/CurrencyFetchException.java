package com.example.bookstore.exception;

/**
 * Exception thrown when fetching currency rates fails.
 */
public class CurrencyFetchException extends RuntimeException {
    public CurrencyFetchException(String message) {
        super(message);
    }
}