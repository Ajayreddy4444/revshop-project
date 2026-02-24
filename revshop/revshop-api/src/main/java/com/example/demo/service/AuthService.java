package com.example.demo.service;


import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request, String role);

    AuthResponse login(LoginRequest request);
    
    void forgotPassword(String email);
    void resetPassword(String email, String otp, String newPassword);
}

