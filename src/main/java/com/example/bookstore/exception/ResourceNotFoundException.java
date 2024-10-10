package com.example.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class for handling resource not found scenarios.
 * This class is used when a requested resource (such as an author or a book) is not found in the database.
 * An HTTP 404 Not Found status is returned when this exception is thrown.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
