package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ForgotPasswordRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.ResetPasswordRequest;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") 
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/register/buyer")
    public AuthResponse registerBuyer(@RequestBody RegisterRequest request){
        return authService.register(request, "BUYER");
    }

    @PostMapping("/register/seller")
    public AuthResponse registerSeller(@RequestBody RegisterRequest request){
        return authService.register(request, "SELLER");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return "OTP sent (check your mail)";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return "Password updated successfully";
    }
}