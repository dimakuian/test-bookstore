package com.example.bookstore.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Component responsible for JWT token operations such as generation, validation, and extraction of username.
 * Utilizes the JWT secret and expiration properties defined in the application's configuration.
 */
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration-date:3600000}")
    private long jwtExpirationDate;

    /**
     * Component responsible for JWT token operations such as generation, validation, and extraction of username.
     * Utilizes the JWT secret and expiration properties defined in the application's configuration.
     */
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    /**
     * Generates a cryptographic signing key from the JWT secret.
     *
     * @return A cryptographic key used for signing JWT tokens.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token The JWT token from which the username is to be extracted.
     * @return The username extracted from the JWT token.
     */
    public String getUsername(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token){
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parse(token);
        return true;
    }
}
