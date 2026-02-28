package com.example.demo.dto;

import com.example.demo.entity.PaymentMethod;

public class PaymentRequestDTO {

    private Long orderId;
    private Double amount;
    private PaymentMethod paymentMethod;

    public PaymentRequestDTO() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}