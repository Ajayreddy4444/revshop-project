package com.example.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.service.PaymentClientService;

@Service
public class PaymentClientServiceImpl implements PaymentClientService {

    private final RestTemplate restTemplate;

    @Value("${backend.base-url}")
    private String baseUrl;

    public PaymentClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void processPayment(PaymentRequest request) {

        String url = baseUrl + "/payments/process";

        try {
            restTemplate.postForObject(url, request, Object.class);
        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException(resolveErrorMessage(ex));
        }
        
    }
    @Override
    public void cancelOrder(Long orderId) {

        String url = baseUrl + "/payments/cancel/" + orderId;

        try {
            restTemplate.postForObject(url, null, String.class);
        } catch (HttpStatusCodeException ex) {
            throw new RuntimeException(resolveErrorMessage(ex));
        }

    }

    private String resolveErrorMessage(HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString();

        if (responseBody == null || responseBody.isBlank()) {
            return "Payment failed. Please try again.";
        }

        String normalized = responseBody.trim();
        String marker = "\"error\":\"";
        int start = normalized.indexOf(marker);
        if (start >= 0) {
            start += marker.length();
            int end = normalized.indexOf("\"", start);
            if (end > start) {
                return normalized.substring(start, end);
            }
        }

        return normalized;
    }
}


