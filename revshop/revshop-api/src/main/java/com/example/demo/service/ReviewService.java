package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.dto.ReviewStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    // Add Review
    ReviewResponse addReview(Long productId, ReviewRequest request);

    // Get Reviews with Pagination
//    Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable);
    List<ReviewResponse> getReviewsByProduct(Long productId);

    // Update Review
    ReviewResponse updateReview(Long reviewId, ReviewRequest request);

    // Delete Review
    void deleteReview(Long reviewId);

    // Review Statistics
    ReviewStatsResponse getReviewStats(Long productId);
    
    //existing review not allowed twice
    boolean hasUserReviewed(Long userId, Long productId);
}