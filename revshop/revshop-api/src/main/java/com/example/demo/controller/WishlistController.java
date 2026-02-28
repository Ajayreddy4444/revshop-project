package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.WishlistResponse;
import com.example.demo.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/toggle/{userId}/{productId}")
    public WishlistResponse toggleWishlist(@PathVariable Long userId, @PathVariable Long productId) {

        boolean added =
                wishlistService.toggleWishlist(
                        userId, productId);

        return new WishlistResponse(productId, added);
    }

    @GetMapping("/my/{userId}")
    public List<Long> getMyWishlist(@PathVariable Long userId) {

        return wishlistService
                .getUserWishlistProductIds(userId);
    }
    
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        wishlistService.removeFromWishlist(userId, productId);

        return ResponseEntity.ok().build();
    }
}