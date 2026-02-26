package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
public class SellerProductPageController {

    private final ProductClientService productClientService;

    public SellerProductPageController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @GetMapping("/products/new")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", productClientService.getAllCategories());

        return "seller-create-product";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute ProductDto product,
                                HttpSession session) {
        var user = (com.example.demo.dto.AuthResponse) session.getAttribute("user");
        productClientService.createProduct(product, user.getId());
        return "redirect:/seller/products";
    }

    @GetMapping("/products")
    public String sellerProducts(HttpSession session, Model model) {
        if (session.getAttribute("user") == null)
            return "redirect:/login";
        var user = (com.example.demo.dto.AuthResponse) session.getAttribute("user");
        model.addAttribute("products", productClientService.getSellerProducts(user.getId()));
        return "seller-products";
    }
}
