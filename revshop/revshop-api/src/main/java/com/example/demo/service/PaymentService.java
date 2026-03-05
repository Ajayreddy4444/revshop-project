package com.example.demo.service;
import com.example.demo.entity.PaymentMethod;
import com.example.demo.entity.Payment;
public interface PaymentService {

	Payment processPayment(Long orderId,
                       Double amount,
                       PaymentMethod method,
                       String cardNumber,
                       String cvv,
                       String upiId,
                       String expiryDate);
    void cancelOrder(Long orderId);
}
