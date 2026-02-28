package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.WishlistResponse;
import com.example.demo.service.ProductClientService;
import com.example.demo.service.WishlistClientService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WishlistPageController {

    private final WishlistClientService wishlistClientService;
    private final ProductClientService productClientService;

    public WishlistPageController(WishlistClientService wishlistClientService,
                                  ProductClientService productClientService) {
        this.wishlistClientService = wishlistClientService;
        this.productClientService = productClientService;
    }

    @GetMapping("/wishlist")
    public String myWishlist(HttpSession session, Model model) {

        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        if (user == null)
            return "redirect:/login";

        // 1️⃣ Get wishlist product IDs
        List<Long> productIds =
                wishlistClientService.getWishlistProductIds(user.getId());

        // 2️⃣ Convert IDs to full ProductDto
        List<ProductDto> products = new ArrayList<>();

        if (productIds != null) {
            for (Long id : productIds) {
                products.add(
                        productClientService.getProductById(id)
                );
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("activePage", "wishlist");

        return "wishlist";
    }
    
}