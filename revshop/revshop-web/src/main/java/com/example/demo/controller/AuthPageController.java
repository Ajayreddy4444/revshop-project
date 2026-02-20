package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthClientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthPageController {

private final AuthClientService authService;

// constructor injection
public AuthPageController(AuthClientService authClientService) {
    this.authService = authClientService;
}

// ---------------- LOGIN PAGE ----------------
@GetMapping("/login")
public String loginPage() {
    return "login";
}

// ---------------- REGISTER PAGE ----------------
@GetMapping("/register")
public String registerPage() {
    return "register";
}

// ---------------- REGISTER SUBMIT ----------------
@PostMapping("/register")
public String registerUser(@ModelAttribute RegisterRequest request,
                           @RequestParam String role,
                           Model model) {

    try {

        if(role.equalsIgnoreCase("buyer"))
            authService.registerBuyer(request);
        else
            authService.registerSeller(request);

        model.addAttribute("success", "Registration successful! Please login.");
        return "login";

    } catch (Exception e) {
        model.addAttribute("error", "Registration failed: " + e.getMessage());
        return "register";
    }
}

// ---------------- LOGIN SUBMIT ----------------
@PostMapping("/login")
public String loginUser(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

    try {

        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(password);

        AuthResponse response = authService.login(req);

        // store full user object in session
        session.setAttribute("user", response);

        return "redirect:/home";

    } catch (Exception e) {
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }
}

// ---------------- LOGOUT ----------------
@GetMapping("/logout")
public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
}


}
