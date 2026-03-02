package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.WishlistResponse;

import java.util.List;

public interface WishlistClientService {

    boolean toggleWishlist(Long productId, Long userId);

    List<Long> getWishlistProductIds(Long userId);

    void removeFromWishlist(Long userId, Long productId);
}