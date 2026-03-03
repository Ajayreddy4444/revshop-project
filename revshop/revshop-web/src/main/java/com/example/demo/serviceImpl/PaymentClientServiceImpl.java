package com.example.demo.serviceImpl;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.service.PaymentClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClientServiceImpl implements PaymentClientService {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${backend.base-url}")
    private String baseUrl;

    public PaymentClientServiceImpl(RestTemplate restTemplate,
                                    HttpServletRequest request) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    // 🔥 Attach JWT Token
    private HttpHeaders getHeaders() {

        String token = (String) request.getSession().getAttribute("token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (token != null) {
            headers.setBearerAuth(token);
        }

        return headers;
    }

    @Override
    public void processPayment(PaymentRequest requestBody) {

        HttpEntity<PaymentRequest> entity =
                new HttpEntity<>(requestBody, getHeaders());

        restTemplate.exchange(
                baseUrl + "/payments/process",
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    @Override
    public void cancelOrder(Long orderId) {

        HttpEntity<Void> entity =
                new HttpEntity<>(getHeaders());

        restTemplate.exchange(
                baseUrl + "/payments/cancel/" + orderId,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }
}