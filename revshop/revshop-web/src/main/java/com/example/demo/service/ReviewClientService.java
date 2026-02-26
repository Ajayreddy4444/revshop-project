package com.example.demo.service;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public ReviewClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ================== GET REVIEWS ==================
    public List<ReviewResponse> getReviewsByProduct(Long productId) {

        try {
            String url = baseUrl + "/reviews/product/" + productId;

            ReviewResponse[] reviews =
                    restTemplate.getForObject(url, ReviewResponse[].class);

            if (reviews == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(reviews);

        } catch (RestClientException ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    // ================== ADD REVIEW ==================
    public void addReview(Long productId, ReviewRequest request) {

        try {
            String url = baseUrl + "/reviews/" + productId;

            restTemplate.postForObject(url, request, ReviewResponse.class);

        } catch (RestClientException ex) {
            ex.printStackTrace();
        }
    }

    // ================== CHECK IF USER REVIEWED ==================
    public boolean hasUserReviewed(Long userId, Long productId) {

        try {
            String url = baseUrl + "/reviews/has-reviewed/"
                    + userId + "/" + productId;

            Boolean result =
                    restTemplate.getForObject(url, Boolean.class);

            return Boolean.TRUE.equals(result);

        } catch (RestClientException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}