package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.AddressRequest;
import com.example.demo.service.AddressClientService;

@Controller
@RequestMapping("/address")
public class AddressPageController {

    private final AddressClientService addressClientService;

    public AddressPageController(AddressClientService addressClientService) {
        this.addressClientService = addressClientService;
    }

    private Long getLoggedInUserId(HttpSession session) {
        AuthResponse user = (AuthResponse) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }

    @GetMapping("/add")
    public String showAddAddressPage(HttpSession session) {

        System.out.println("SESSION USER (GET): " + session.getAttribute("user"));

        return "add-address";
    }

    @PostMapping("/save")
    public String saveAddress(AddressRequest request, HttpSession session) {

        System.out.println("SESSION USER (POST): " + session.getAttribute("user"));

        Long userId = getLoggedInUserId(session);

        if (userId == null) {
            System.out.println("User is NULL → Redirecting");
            return "redirect:/login";
        }

        request.setUserId(userId);

        addressClientService.saveAddress(request);

        return "redirect:/orders/checkout";
    }
}