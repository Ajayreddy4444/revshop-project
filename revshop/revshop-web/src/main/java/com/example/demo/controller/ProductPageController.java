package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ReviewRequest;

import com.example.demo.service.ProductClientService;
import com.example.demo.service.ReviewClientService;
import com.example.demo.service.OrderClientService;

import com.example.demo.service.WishlistClientService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    private final ProductClientService productClientService;
    private final ReviewClientService reviewClientService;
    private final OrderClientService orderClientService;
    private final WishlistClientService wishlistClientService;

    public ProductPageController(ProductClientService productClientService,
                                 ReviewClientService reviewClientService,
                                 OrderClientService orderClientService,
                                 WishlistClientService wishlistClientService) {
        this.productClientService = productClientService;
        this.reviewClientService = reviewClientService;
        this.orderClientService = orderClientService;
        this.wishlistClientService = wishlistClientService;
    }

    @GetMapping
    public String productList(HttpSession session,
                              @RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Double minPrice,
                              @RequestParam(required = false) Double maxPrice,
                              Model model) {
        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        if (user == null)
            return "redirect:/login";

        var products = productClientService.searchProducts(
                keyword, categoryId, minPrice, maxPrice
        );

        var wishlistIds = wishlistClientService.getWishlistProductIds(user.getId());

        products.forEach(product ->
                product.setWishlisted(
                        wishlistIds.contains(product.getId())
                )
        );

        model.addAttribute("products", products);
        model.addAttribute("categories", productClientService.getAllCategories());

        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("activePage", "products");

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

        var product = productClientService.getProductById(id);

        var wishlistIds = wishlistClientService.getWishlistProductIds(user.getId());

        product.setWishlisted(
                wishlistIds.contains(product.getId())
        );

        model.addAttribute("product", product);

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