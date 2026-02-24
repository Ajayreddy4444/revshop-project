package com.example.demo.service;

import com.example.demo.dto.PaymentRequestDTO;

public interface PaymentClientService {

    void processPayment(PaymentRequestDTO request);
}