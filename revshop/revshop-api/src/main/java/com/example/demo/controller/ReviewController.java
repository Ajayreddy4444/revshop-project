package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.dto.ReviewStatsResponse;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ✅ Add Review
    @PostMapping("/{productId}")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.addReview(productId, request)
        );
    }

    // ✅ Get Reviews (Pagination)
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<Page<ReviewResponse>> getReviews(
//            @PathVariable Long productId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "5") int size) {
//
//        return ResponseEntity.ok(
//                reviewService.getReviewsByProduct(
//                        productId,
//                        PageRequest.of(page, size)
//                )
//        );
//    }
    
    @GetMapping("/product/{productId}")
    public List<ReviewResponse> getReviewsByProduct(
            @PathVariable Long productId) {

        return reviewService.getReviewsByProduct(productId);
    }

    // ✅ Update Review
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.updateReview(reviewId, request)
        );
    }

    // ✅ Delete Review
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }

    // ✅ Review Statistics
    @GetMapping("/stats/{productId}")
    public ResponseEntity<ReviewStatsResponse> getStats(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                reviewService.getReviewStats(productId)
        );
    }
    
    //duplicate review disabled
    @GetMapping("/has-reviewed/{userId}/{productId}")
    public boolean hasUserReviewed(@PathVariable Long userId,
                                   @PathVariable Long productId) {
        return reviewService.hasUserReviewed(userId, productId);
    }
}