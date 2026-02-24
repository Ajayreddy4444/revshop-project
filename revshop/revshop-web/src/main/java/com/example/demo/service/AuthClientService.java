package com.example.demo.service;

import com.example.demo.dto.*;

public interface AuthClientService {

AuthResponse login(LoginRequest request);

AuthResponse registerBuyer(RegisterRequest request);

AuthResponse registerSeller(RegisterRequest request);

void forgotPassword(String email);
void resetPassword(String email, String otp, String newPassword);

}
