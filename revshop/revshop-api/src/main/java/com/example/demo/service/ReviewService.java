package com.example.demo.service;

import com.example.demo.entity.Review;
import com.example.demo.entity.User;

import java.util.List;

public interface ReviewService {

  //  Review addReview(Long productId, int rating, String comment, Long userID);
	Review addReview(Long productId, int rating, String comment, Long userId);
	

    List<Review> getReviewsByProduct(Long productId);
}