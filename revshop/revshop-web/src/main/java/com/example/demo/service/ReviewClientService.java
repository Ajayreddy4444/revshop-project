package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;

import java.util.List;

public interface ReviewClientService {

    // Get reviews for a product
    List<ReviewResponse> getReviewsByProduct(Long productId);

    // Add review
    void addReview(Long productId, ReviewRequest request);

    // Check if user already reviewed
    boolean hasUserReviewed(Long userId, Long productId);

    // Delete review (new feature)
    void deleteReview(Long reviewId, Long userId);

}
