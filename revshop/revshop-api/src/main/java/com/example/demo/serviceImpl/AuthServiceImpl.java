package com.example.demo.serviceImpl;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository,
            JwtUtil jwtUtil,
            PasswordResetTokenRepository passwordResetTokenRepository,
            EmailService emailService) {

this.userRepository = userRepository;
this.jwtUtil = jwtUtil;
this.passwordResetTokenRepository = passwordResetTokenRepository;
this.emailService = emailService;
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

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return mapToResponse(user, token);
    }

    private AuthResponse mapToResponse(User user, String token){
        AuthResponse response = new AuthResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(token); 

        return response;
    }
    
    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        PasswordResetToken token = new PasswordResetToken(
                email,
                otp,
                LocalDateTime.now().plusMinutes(5),
                false
        );

        passwordResetTokenRepository.save(token);

        emailService.sendOtpEmail(email, otp);  
        
    }
    
    @Override
    public void resetPassword(String email, String otp, String newPassword) {

        PasswordResetToken token = passwordResetTokenRepository
                .findByEmailAndOtpAndUsedFalse(email, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);

        token.setUsed(true);
        passwordResetTokenRepository.save(token);
    }
}