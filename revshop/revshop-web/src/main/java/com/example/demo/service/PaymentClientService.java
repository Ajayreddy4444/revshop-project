package com.example.demo.service;

import com.example.demo.dto.PaymentRequest;

public interface PaymentClientService {

    void processPayment(PaymentRequest request);
    void cancelOrder(Long orderId);
}