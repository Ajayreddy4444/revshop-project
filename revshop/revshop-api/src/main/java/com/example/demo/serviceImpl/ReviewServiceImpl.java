package com.example.demo.serviceImpl;

import com.example.demo.dto.*;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ReviewService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             ProductRepository productRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ✅ ADD REVIEW
    @Override
    public ReviewResponse addReview(Long productId, ReviewRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException("Already reviewed");
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        updateProductRating(product);

        return mapToResponse(review);
    }

    // ✅ GET REVIEWS (PAGINATION)
//    @Override
//    public Page<ReviewResponse> getReviewsByProduct(Long productId, Pageable pageable) {
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Page<Review> reviews = reviewRepository.findByProduct(product, pageable);
//
//        return reviews.map(this::mapToResponse);
//    }
    
    @Override
    public List<ReviewResponse> getReviewsByProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Review> reviews = reviewRepository.findByProduct(product);

        return reviews.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ UPDATE REVIEW
    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        updateProductRating(review.getProduct());

        return mapToResponse(review);
    }

    // ✅ DELETE REVIEW
    @Override
    public void deleteReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Product product = review.getProduct();

        reviewRepository.delete(review);

        updateProductRating(product);
    }

    // ✅ REVIEW STATS
    @Override
    public ReviewStatsResponse getReviewStats(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        long total = reviewRepository.countByProduct(product);

        double avg = reviewRepository.findByProduct(product)
                .stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        return new ReviewStatsResponse(productId, total, avg);
    }

    // 🔥 UPDATE PRODUCT RATING
    private void updateProductRating(Product product) {

        List<Review> reviews = reviewRepository.findByProduct(product);

        double avg = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);

        product.setAverageRating(avg);
        product.setReviewCount(reviews.size());

        productRepository.save(product);
    }

    // 🔥 MAP ENTITY → DTO
    private ReviewResponse mapToResponse(Review review) {

        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setUserId(review.getUser().getId());
        response.setUserName(review.getUser().getName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());

        return response;
    }
    
    //existing review not allowed
    @Override
    public boolean hasUserReviewed(Long userId, Long productId) {
        return reviewRepository
                .existsByUser_IdAndProduct_Id(userId, productId);
    }
}