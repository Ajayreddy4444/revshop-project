package com.example.demo.serviceImpl;

import com.example.demo.entity.Wishlist;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public boolean toggleWishlist(Long userId, Long productId) {

        var existing = wishlistRepository
                .findByUserIdAndProductId(userId, productId);

        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return false; // removed
        } else {
            wishlistRepository.save(
                    new Wishlist(userId, productId)
            );
            return true; // added
        }
    }

    @Override
    public List<Long> getUserWishlistProductIds(Long userId) {

        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getProductId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isWishlisted(Long userId, Long productId) {

        return wishlistRepository
                .findByUserIdAndProductId(userId, productId)
                .isPresent();
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {

        wishlistRepository
                .findByUserIdAndProductId(userId, productId)
                .ifPresent(wishlistRepository::delete);
    }
}