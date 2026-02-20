package com.example.demo.service;

import com.example.demo.dto.*;

public interface AuthClientService {

AuthResponse login(LoginRequest request);

AuthResponse registerBuyer(RegisterRequest request);

AuthResponse registerSeller(RegisterRequest request);

}
