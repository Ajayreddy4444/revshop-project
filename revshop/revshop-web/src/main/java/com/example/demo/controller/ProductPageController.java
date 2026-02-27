package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ReviewRequest;

import com.example.demo.service.ProductClientService;
import com.example.demo.service.ReviewClientService;
import com.example.demo.service.OrderClientService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    private final ProductClientService productClientService;
    private final ReviewClientService reviewClientService;
    private final OrderClientService orderClientService;

    public ProductPageController(ProductClientService productClientService,
                                 ReviewClientService reviewClientService,
                                 OrderClientService orderClientService) {
        this.productClientService = productClientService;
        this.reviewClientService = reviewClientService;
        this.orderClientService = orderClientService;
    }

    // ================== PRODUCT LIST ==================
    @GetMapping
    public String productList(HttpSession session, Model model) {

        if (session.getAttribute("user") == null)
            return "redirect:/login";

        model.addAttribute("products",
                productClientService.getAllProducts());

        return "products";
    }

    // ================== PRODUCT DETAILS ==================
    @GetMapping("/{id}")
    public String productDetails(@PathVariable Long id,
                                 HttpSession session,
                                 Model model) {

        if (session.getAttribute("user") == null)
            return "redirect:/login";

        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        // 1️⃣ Load product
        model.addAttribute("product",
                productClientService.getProductById(id));

        // 2️⃣ Load reviews
        model.addAttribute("reviews",
                reviewClientService.getReviewsByProduct(id));

        // 3️⃣ Check if purchased
        boolean hasPurchased =
                orderClientService.hasUserPurchasedProduct(
                        user.getId(), id);

        // 4️⃣ Check if already reviewed
        boolean hasReviewed =
                reviewClientService.hasUserReviewed(
                        user.getId(), id);

        // 5️⃣ Send ALL required flags to UI
        model.addAttribute("hasPurchased", hasPurchased);
        model.addAttribute("hasReviewed", hasReviewed);
        model.addAttribute("canReview",
                hasPurchased && !hasReviewed);

        return "product-details";
    }

    // ================== ADD REVIEW ==================
    @PostMapping("/{id}/review")
    public String addReview(@PathVariable Long id,
                            @RequestParam Integer rating,
                            @RequestParam String comment,
                            HttpSession session) {

        if (session.getAttribute("user") == null)
            return "redirect:/login";

        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        ReviewRequest request = new ReviewRequest();
        request.setUserId(user.getId());
        request.setRating(rating);
        request.setComment(comment);

        reviewClientService.addReview(id, request);

        return "redirect:/products/" + id;
    }
   
}