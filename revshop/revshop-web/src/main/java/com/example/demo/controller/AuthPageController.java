package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthClientService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthPageController {

    private final AuthClientService authClientService;

    public AuthPageController(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    // ================= LOGIN =================

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            AuthResponse response = authClientService.login(request);

            session.setAttribute("user", response);
            session.setAttribute("userId", response.getId());

            return "redirect:/home";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // ================= REGISTER =================

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model){

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {

            if ("BUYER".equalsIgnoreCase(request.getRole())) {
                authClientService.registerBuyer(request);
            } else {
                authClientService.registerSeller(request);
            }

            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login.");

            return "redirect:/login";

        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================= FORGOT PASSWORD =================

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

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(required = false) String email,
                                    Model model) {
        model.addAttribute("email", email);
        return "reset-password";
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

            return "redirect:/reset-password?email=" + email;
        }
    }
}