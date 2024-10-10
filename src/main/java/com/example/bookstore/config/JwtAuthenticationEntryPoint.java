package com.example.bookstore.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Component that handles unauthorized access attempts.
 * When a user tries to access a secured REST resource without providing any credentials,
 * this entry point is triggered, sending an HTTP 401 Unauthorized response.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Responds to unauthorized access attempts by sending an HTTP 401 Unauthorized response.
     *
     * @param request The request that resulted in an AuthenticationException.
     * @param response The response, where the 401 status code will be set.
     * @param authException The exception that was thrown, indicating that the user was not authenticated.
     * @throws IOException If an input or output exception occurs.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
