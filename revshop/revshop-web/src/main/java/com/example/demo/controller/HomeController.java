package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

@GetMapping("/")
public String landing(HttpSession session) {

    if(session.getAttribute("user") != null) {
        return "redirect:/home";
    }

    return "home";
}

@GetMapping("/home")
public String dashboard(HttpSession session) {

    if(session.getAttribute("user") == null) {
        return "redirect:/login";
    }

    return "dashboard";
}

}
