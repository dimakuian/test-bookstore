package com.example.bookstore.model;

import lombok.Data;

/**
 * Represents the data transfer object for user login credentials.
 * This class is used to encapsulate the necessary information for logging in a user, specifically their username and password.
 */
@Data
public class LoginDto {

    private String username;
    private String password;
}
