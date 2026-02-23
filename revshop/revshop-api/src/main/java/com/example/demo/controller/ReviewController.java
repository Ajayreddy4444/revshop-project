package com.example.demo.controller;

import com.example.demo.dto.*;

import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // Manual constructor (instead of Lombok)
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<Review> addReview(
            @PathVariable Long productId,
            @RequestBody ReviewRequest request) {

        return ResponseEntity.ok(
                reviewService.addReview(
                        productId,
                        request.getRating(),
                        request.getComment(),
                        request.getUserId()
                )
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviews(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                reviewService.getReviewsByProduct(productId)
        );
    }
}