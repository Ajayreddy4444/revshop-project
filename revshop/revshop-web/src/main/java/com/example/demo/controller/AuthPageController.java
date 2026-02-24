package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthClientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthPageController {

private final AuthClientService authClientService;

public AuthPageController(AuthClientService authClientService) {
    this.authClientService = authClientService;
}

@GetMapping("/login")
public String loginPage() {
    return "login";
}

@GetMapping("/register")
public String registerPage() {
    return "register";
}

@PostMapping("/register")
public String registerUser(@ModelAttribute RegisterRequest request,
                           @RequestParam String role,
                           Model model) {

    try {

        if(role.equalsIgnoreCase("buyer"))
        	authClientService.registerBuyer(request);
        else
        	authClientService.registerSeller(request);

        model.addAttribute("success", "Registration successful! Please login.");
        return "login";

    } catch (Exception e) {
        model.addAttribute("error", "Registration failed: " + e.getMessage());
        return "register";
    }
}

@PostMapping("/login")
public String loginUser(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

    try {

        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(password);

        AuthResponse response = authClientService.login(req);

        session.setAttribute("user", response);

        return "redirect:/home";

    } catch (Exception e) {
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }
}

@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
}

@GetMapping("/forgot-password")
public String forgotPasswordPage() {
    return "forgot-password";
}

@PostMapping("/forgot-password")
public String processForgotPassword(@RequestParam String email, Model model) {
    try {
    	authClientService.forgotPassword(email);
        model.addAttribute("success", "OTP sent to your email.");
        model.addAttribute("email", email);
        return "reset-password";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        return "forgot-password";
    }
}

@PostMapping("/reset-password")
public String processResetPassword(@RequestParam String email,
                                   @RequestParam String otp,
                                   @RequestParam String newPassword,
                                   RedirectAttributes redirectAttributes) {
    try {
        authClientService.resetPassword(email, otp, newPassword);

        redirectAttributes.addFlashAttribute("success",
                "Password reset successfully! Please login.");

        return "redirect:/login";

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/reset-password";
    }
}


}
