package com.example.demo.serviceImplTest;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.serviceImpl.ReviewClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ReviewClientServiceImpl reviewClientService;

    private final String baseUrl = "http://localhost:8080/api";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // inject baseUrl manually
        ReflectionTestUtils.setField(reviewClientService, "baseUrl", baseUrl);
    }

    // ✅ GET REVIEWS SUCCESS
    @Test
    void getReviewsByProduct_shouldReturnReviews() {

        ReviewResponse r1 = new ReviewResponse();
        r1.setComment("Good");

        ReviewResponse r2 = new ReviewResponse();
        r2.setComment("Nice");

        ReviewResponse[] response = {r1, r2};

        when(restTemplate.getForObject(
                baseUrl + "/reviews/product/1",
                ReviewResponse[].class))
                .thenReturn(response);

        List<ReviewResponse> result =
                reviewClientService.getReviewsByProduct(1L);

        assertEquals(2, result.size());
        verify(restTemplate)
                .getForObject(baseUrl + "/reviews/product/1", ReviewResponse[].class);
    }

    // ✅ GET REVIEWS EMPTY
    @Test
    void getReviewsByProduct_shouldReturnEmptyList() {

        when(restTemplate.getForObject(
                baseUrl + "/reviews/product/1",
                ReviewResponse[].class))
                .thenReturn(null);

        List<ReviewResponse> result =
                reviewClientService.getReviewsByProduct(1L);

        assertTrue(result.isEmpty());
    }

    // ✅ ADD REVIEW
    @Test
    void addReview_shouldCallBackendApi() {

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Great");

        reviewClientService.addReview(1L, request);

        verify(restTemplate).postForObject(
                baseUrl + "/reviews/1",
                request,
                ReviewResponse.class
        );
    }

    // ✅ HAS USER REVIEWED TRUE
    @Test
    void hasUserReviewed_shouldReturnTrue() {

        when(restTemplate.getForObject(
                baseUrl + "/reviews/has-reviewed/1/2",
                Boolean.class))
                .thenReturn(true);

        boolean result =
                reviewClientService.hasUserReviewed(1L, 2L);

        assertTrue(result);
    }

    // ✅ DELETE REVIEW
    @Test
    void deleteReview_shouldCallDeleteApi() {

        reviewClientService.deleteReview(1L, 2L);

        verify(restTemplate).delete(
                baseUrl + "/reviews/1?userId=2"
        );
    }
}