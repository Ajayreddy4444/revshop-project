package com.example.demo.service;

import java.util.List;

public interface WishlistService {

    boolean toggleWishlist(Long userId, Long productId);

    List<Long> getUserWishlistProductIds(Long userId);

    boolean isWishlisted(Long userId, Long productId);

    void removeFromWishlist(Long userId, Long productId);
}