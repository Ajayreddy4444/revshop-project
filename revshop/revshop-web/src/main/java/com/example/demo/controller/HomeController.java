package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landing(HttpSession session) {

        Object userObj = session.getAttribute("user");

        if (userObj != null) {

            var user = (AuthResponse) userObj;

            if ("BUYER".equals(user.getRole())) {
                return "redirect:/products";
            }

            if ("SELLER".equals(user.getRole())) {
                return "redirect:/seller/products";
            }
        }

        return "home";
    }

    @GetMapping("/home")
    public String dashboard(HttpSession session) {

        Object userObj = session.getAttribute("user");

        if (userObj == null) {
            return "redirect:/login";
        }

        var user = (AuthResponse) userObj;

        if ("BUYER".equals(user.getRole())) {
            return "redirect:/products";
        }

        if ("SELLER".equals(user.getRole())) {
            return "redirect:/seller/products";
        }

        return "redirect:/";
    }

}
