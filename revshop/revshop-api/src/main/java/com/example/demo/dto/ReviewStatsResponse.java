package com.example.demo.dto;

public class ReviewStatsResponse {

    private Long productId;
    private long totalReviews;
    private double averageRating;

    public ReviewStatsResponse(Long productId, long totalReviews, double averageRating) {
        this.productId = productId;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
    }

    public Long getProductId() {
        return productId;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }
}