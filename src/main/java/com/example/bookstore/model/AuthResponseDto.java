package com.example.bookstore.model;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for carrying authentication response details.
 * This class is primarily used to encapsulate the access token provided upon successful authentication.
 */
@Data
public class AuthResponseDto {

    private String accessToken;
}
