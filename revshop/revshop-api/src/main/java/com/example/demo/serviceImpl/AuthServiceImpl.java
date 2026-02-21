package com.example.demo.serviceImpl;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;   // ⭐ NEW

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse register(RegisterRequest request, String roleStr) {

        Role role = Role.valueOf(roleStr.toUpperCase());

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                role,
                true
        );

        userRepository.save(user);

        // token also generated after register (optional but good)
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return mapToResponse(user, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        // ⭐ GENERATE TOKEN AFTER SUCCESSFUL LOGIN
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return mapToResponse(user, token);
    }

    private AuthResponse mapToResponse(User user, String token){
        AuthResponse response = new AuthResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token); // ⭐ ADD TOKEN

        return response;
    }
}