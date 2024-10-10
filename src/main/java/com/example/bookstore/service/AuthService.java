package com.example.bookstore.service;

import com.example.bookstore.model.LoginDto;

public interface AuthService {

    String login(LoginDto loginDto);
}
