package com.example.demo.controller;

import com.example.demo.dto.Category;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seller/categories")
public class SellerCategoryPageController {
    private final ProductClientService productClientService;

    public SellerCategoryPageController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @GetMapping("/new")
    public String showAddCategoryPage(HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        return "seller-add-category";
    }


    @PostMapping("/add")
    public String addCategory(@RequestParam String name,
                              @RequestParam(required = false) String description) {

        try {
            productClientService.createCategory(name, description);
            return "redirect:/seller/products/new";
        } catch (Exception e) {
            return "redirect:/seller/categories/new?error=exists";
        }
    }
}
