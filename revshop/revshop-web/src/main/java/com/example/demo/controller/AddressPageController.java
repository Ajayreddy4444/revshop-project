package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.AddressRequest;
import com.example.demo.dto.AddressResponse;
import com.example.demo.service.AddressClientService;

@Controller
@RequestMapping("/address")
public class AddressPageController {

    private final AddressClientService addressClientService;

    public AddressPageController(AddressClientService addressClientService) {
        this.addressClientService = addressClientService;
    }

    // 🔹 Get logged-in user from session
    private Long getLoggedInUserId(HttpSession session) {
        AuthResponse user = (AuthResponse) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }

    // 🔹 Show Add Address Page
    @GetMapping("/add")
    public String showAddAddressPage(Model model) {
        model.addAttribute("address", new AddressRequest());
        return "add-address";
    }

    // 🔹 Save New Address
    @PostMapping("/save")
    public String saveAddress(@ModelAttribute AddressRequest request,
                              HttpSession session) {

        Long userId = getLoggedInUserId(session);

        if (userId == null) {
            return "redirect:/login";
        }

        request.setUserId(userId);

        addressClientService.saveAddress(request);

        return "redirect:/orders/checkout";
    }

    // 🔹 Show Edit Address Page
    @GetMapping("/edit/{id}")
    public String editAddress(@PathVariable Long id,
                              HttpSession session,
                              Model model) {

        Long userId = getLoggedInUserId(session);

        if (userId == null) {
            return "redirect:/login";
        }

        AddressResponse address = addressClientService.getAddressById(id);

        model.addAttribute("address", address);

        return "edit-address";
    }

    // 🔹 Update Address
    @PostMapping("/update")
    public String updateAddress(@ModelAttribute AddressRequest request,
                                HttpSession session) {

        Long userId = getLoggedInUserId(session);

        if (userId == null) {
            return "redirect:/login";
        }

        request.setUserId(userId);

        addressClientService.updateAddress(request);

        return "redirect:/orders/checkout";
    }
}