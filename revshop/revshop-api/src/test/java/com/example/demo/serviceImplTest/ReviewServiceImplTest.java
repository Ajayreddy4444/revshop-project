package com.example.demo.serviceImplTest;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.serviceImpl.ReviewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    // ✅ ADD REVIEW SUCCESS
    @Test
    void addReview_shouldAddSuccessfully() {

        Product product = new Product();
        product.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setName("John");

        ReviewRequest request = new ReviewRequest();
        request.setUserId(2L);
        request.setRating(5);
        request.setComment("Great!");

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(reviewRepository.findByUserAndProduct(user, product))
                .thenReturn(Optional.empty());

        when(reviewRepository.findByProduct(product))
                .thenReturn(List.of()); // for updateProductRating

        ReviewResponse response =
                reviewService.addReview(1L, request);

        assertEquals(5, response.getRating());
        verify(reviewRepository).save(any(Review.class));
        verify(productRepository).save(product);
    }

    // ✅ ALREADY REVIEWED
    @Test
    void addReview_shouldThrow_whenAlreadyReviewed() {

        Product product = new Product();
        User user = new User();

        ReviewRequest request = new ReviewRequest();
        request.setUserId(2L);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(reviewRepository.findByUserAndProduct(user, product))
                .thenReturn(Optional.of(new Review()));

        assertThrows(RuntimeException.class,
                () -> reviewService.addReview(1L, request));
    }

    // ✅ GET REVIEWS
    @Test
    void getReviewsByProduct_shouldReturnList() {

        Product product = new Product();
        product.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setName("John");

        Review review = new Review();
        review.setId(1L);
        review.setUser(user);
        review.setRating(4);
        review.setComment("Good");
        review.setProduct(product);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(reviewRepository.findByProduct(product))
                .thenReturn(List.of(review));

        List<ReviewResponse> result =
                reviewService.getReviewsByProduct(1L);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getUserName());
    }

    @Test
    void deleteReview_shouldDeleteAndUpdateRating() {

        Product product = new Product();
        product.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setProduct(product);

        when(reviewRepository.findByIdAndUser_Id(1L, 2L))
                .thenReturn(Optional.of(review));

        reviewService.deleteReview(1L, 2L);

        verify(reviewRepository).delete(review);
    }

    // ✅ REVIEW STATS
    @Test
    void getReviewStats_shouldReturnCorrectStats() {

        Product product = new Product();
        product.setId(1L);

        Review r1 = new Review();
        r1.setRating(4);

        Review r2 = new Review();
        r2.setRating(2);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(reviewRepository.countByProduct(product))
                .thenReturn(2L);

        when(reviewRepository.findByProduct(product))
                .thenReturn(List.of(r1, r2));

        ReviewStatsResponse stats =
                reviewService.getReviewStats(1L);

        assertEquals(2L, stats.getTotalReviews());
        assertEquals(3.0, stats.getAverageRating());
    }

    // ✅ HAS USER REVIEWED
    @Test
    void hasUserReviewed_shouldReturnTrue() {

        when(reviewRepository.existsByUser_IdAndProduct_Id(1L, 2L))
                .thenReturn(true);

        assertTrue(reviewService.hasUserReviewed(1L, 2L));
    }
}