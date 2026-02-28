package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.WishlistResponse;
import com.example.demo.service.WishlistClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistClientController {

    private final WishlistClientService wishlistClientService;

    public WishlistClientController(WishlistClientService wishlistClientService) {
        this.wishlistClientService = wishlistClientService;
    }

    @PostMapping("/toggle/{productId}")
    public WishlistResponse toggleWishlist(
            @PathVariable Long productId,
            HttpSession session) {

        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        boolean added =
                wishlistClientService.toggleWishlist(
                        user.getId(), productId);

        return new WishlistResponse(productId, added);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long productId,
            HttpSession session) {

        AuthResponse user =
                (AuthResponse) session.getAttribute("user");

        wishlistClientService.removeFromWishlist(
                user.getId(), productId);

        return ResponseEntity.ok().build();
    }
}
