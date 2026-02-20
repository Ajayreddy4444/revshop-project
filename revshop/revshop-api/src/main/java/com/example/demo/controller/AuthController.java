package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow frontend later
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // REGISTER BUYER
    @PostMapping("/register/buyer")
    public AuthResponse registerBuyer(@RequestBody RegisterRequest request){
        return authService.register(request, "BUYER");
    }

    // REGISTER SELLER
    @PostMapping("/register/seller")
    public AuthResponse registerSeller(@RequestBody RegisterRequest request){
        return authService.register(request, "SELLER");
    }

    // LOGIN
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }
}