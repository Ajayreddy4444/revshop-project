package com.example.demo.dto;

public class WishlistResponse {

    private Long productId;
    private boolean added;

    public WishlistResponse(Long productId, boolean added) {
        this.productId = productId;
        this.added = added;
    }

    public Long getProductId() {
        return productId;
    }

    public boolean isAdded() {
        return added;
    }
}