package com.example.demo.serviceImplTest;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.EmailService;
import com.example.demo.serviceImpl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Ajay");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPass");
        user.setRole(Role.BUYER);
    }

    // ================= REGISTER =================

    @Test
    void register_success() {

        RegisterRequest request = new RegisterRequest();
        request.setName("Ajay");
        request.setEmail("test@gmail.com");
        request.setPassword("123456");
        request.setPhone("9999999999");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPass");

        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        var response = authService.register(request, "BUYER");

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_emailAlreadyExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(request, "BUYER"));
    }

    // ================= LOGIN =================

    @Test
    void login_success() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123456", "encodedPass"))
                .thenReturn(true);

        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        var response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_userNotFound() {

        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void login_invalidPassword() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("wrong");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encodedPass"))
                .thenReturn(false);

        assertThrows(InvalidPasswordException.class,
                () -> authService.login(request));
    }

    // ================= FORGOT PASSWORD =================

    @Test
    void forgotPassword_success() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        authService.forgotPassword("test@gmail.com");

        verify(passwordResetTokenRepository, times(1))
                .save(any(PasswordResetToken.class));

        verify(emailService, times(1))
                .sendOtpEmail(eq("test@gmail.com"), anyString());
    }

    @Test
    void forgotPassword_userNotFound() {

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.forgotPassword("unknown@gmail.com"));
    }

    // ================= RESET PASSWORD =================

    @Test
    void resetPassword_success() {

        PasswordResetToken token = new PasswordResetToken(
                "test@gmail.com",
                "123456",
                LocalDateTime.now().plusMinutes(5),
                false
        );

        when(passwordResetTokenRepository
                .findByEmailAndOtpAndUsedFalse("test@gmail.com", "123456"))
                .thenReturn(Optional.of(token));

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newpass"))
                .thenReturn("encodedNewPass");

        authService.resetPassword("test@gmail.com", "123456", "newpass");

        verify(userRepository, times(1)).save(user);
        verify(passwordResetTokenRepository, times(1)).save(token);
        assertTrue(token.isUsed());
    }

    @Test
    void resetPassword_invalidOtp() {

        when(passwordResetTokenRepository
                .findByEmailAndOtpAndUsedFalse("test@gmail.com", "999999"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidOtpException.class,
                () -> authService.resetPassword("test@gmail.com", "999999", "pass"));
    }
}