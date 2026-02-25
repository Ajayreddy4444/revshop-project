package com.example.demo.serviceImpl;

import com.example.demo.dto.*;
import com.example.demo.service.AuthClientService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthClientServiceImpl implements AuthClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public AuthClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request);
       
        ResponseEntity<AuthResponse> response =
                restTemplate.postForEntity(
                        baseUrl + "/auth/login",
                        entity,
                        AuthResponse.class
                );

        return response.getBody();
    }

    @Override
    public AuthResponse registerBuyer(RegisterRequest request) {

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request);

        ResponseEntity<AuthResponse> response =
                restTemplate.postForEntity(
                        baseUrl + "/auth/register/buyer",
                        entity,
                        AuthResponse.class
                );

        return response.getBody();
    }

    @Override
    public AuthResponse registerSeller(RegisterRequest request) {

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request);

        ResponseEntity<AuthResponse> response =
                restTemplate.postForEntity(
                        baseUrl + "/auth/register/seller",
                        entity,
                        AuthResponse.class
                );

        return response.getBody();
    }
    
    @Override
    public void forgotPassword(String email) {

        String url = "http://localhost:8080/api/auth/forgot-password";

        Map<String, String> request = Map.of("email", email);

        restTemplate.postForObject(url, request, String.class);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {

        String url = "http://localhost:8080/api/auth/reset-password";

        Map<String, String> request = Map.of(
                "email", email,
                "otp", otp,
                "newPassword", newPassword
        );

        restTemplate.postForObject(url, request, String.class);
    }
}