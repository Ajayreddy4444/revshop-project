package com.example.demo.serviceImpl;

import com.example.demo.dto.*;
import com.example.demo.service.AuthClientService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
        try {

            HttpEntity<LoginRequest> entity = new HttpEntity<>(request);

            ResponseEntity<AuthResponse> response =
                    restTemplate.postForEntity(
                            baseUrl + "/auth/login",
                            entity,
                            AuthResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(extractErrorMessage(ex));
        }
    }

    @Override
    public AuthResponse registerBuyer(RegisterRequest request) {
        try {

            HttpEntity<RegisterRequest> entity = new HttpEntity<>(request);

            ResponseEntity<AuthResponse> response =
                    restTemplate.postForEntity(
                            baseUrl + "/auth/register/buyer",
                            entity,
                            AuthResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(extractErrorMessage(ex));
        }
    }

    @Override
    public AuthResponse registerSeller(RegisterRequest request) {
        try {

            HttpEntity<RegisterRequest> entity = new HttpEntity<>(request);

            ResponseEntity<AuthResponse> response =
                    restTemplate.postForEntity(
                            baseUrl + "/auth/register/seller",
                            entity,
                            AuthResponse.class
                    );

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(extractErrorMessage(ex));
        }
    }

    @Override
    public void forgotPassword(String email) {
        try {

            Map<String, String> request = Map.of("email", email);

            restTemplate.postForObject(
                    baseUrl + "/auth/forgot-password",
                    request,
                    String.class
            );

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(extractErrorMessage(ex));
        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        try {

            Map<String, String> request = Map.of(
                    "email", email,
                    "otp", otp,
                    "newPassword", newPassword
            );

            restTemplate.postForObject(
                    baseUrl + "/auth/reset-password",
                    request,
                    String.class
            );

        } catch (HttpClientErrorException ex) {
            throw new RuntimeException(extractErrorMessage(ex));
        }
    }

    // ✅ CLEAN ERROR EXTRACTION
    private String extractErrorMessage(HttpClientErrorException ex) {

        String response = ex.getResponseBodyAsString();

        if (response == null || response.isEmpty()) {
            return "Something went wrong";
        }

        response = response.replace("{", "")
                           .replace("}", "")
                           .replace("\"", "");

        StringBuilder message = new StringBuilder();

        String[] pairs = response.split(",");

        for (String pair : pairs) {
            if (pair.contains(":")) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    message.append(keyValue[1].trim()).append(" ");
                }
            }
        }

        return message.toString().trim();
    }
}