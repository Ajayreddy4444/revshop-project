package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

// PUBLIC LANDING PAGE
@GetMapping("/")
public String landing(HttpSession session) {

    // If already logged in → go dashboard
    if(session.getAttribute("user") != null) {
        return "redirect:/home";
    }

    return "home";
}

// AFTER LOGIN
@GetMapping("/home")
public String dashboard(HttpSession session) {

    if(session.getAttribute("user") == null) {
        return "redirect:/login";
    }

    return "dashboard";
}

}
