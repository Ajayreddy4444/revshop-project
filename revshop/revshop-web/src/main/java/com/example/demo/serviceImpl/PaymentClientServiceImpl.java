package com.example.demo.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.PaymentRequestDTO;
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
    public void processPayment(PaymentRequestDTO request) {

        String url = baseUrl + "/payments/process";

        restTemplate.postForObject(url, request, Object.class);
    }
}