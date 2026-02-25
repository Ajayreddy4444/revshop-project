package com.example.demo.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentMethod;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment processPayment(Long orderId,
                                  Double amount,
                                  PaymentMethod method) {

        // 🔹 Basic Validation
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount");
        }

        if (method == null) {
            throw new IllegalArgumentException("Payment method must be selected");
        }

        // 🔹 Simulate payment gateway logic
        // In real systems this calls Razorpay / Stripe / etc.
        PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

        // 🔹 Create Payment entity
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus(paymentStatus);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }
}