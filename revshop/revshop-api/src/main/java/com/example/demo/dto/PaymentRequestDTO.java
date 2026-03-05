package com.example.demo.dto;

import com.example.demo.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class PaymentRequestDTO {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Pattern(
        regexp = "^(?!0+$)(?!([0-9])\\1+$)[0-9]{16}$",
        message = "Card number must be 16 digits and cannot be all same numbers"
    )
    private String cardNumber;

    @Pattern(
        regexp = "^(?!000$)[0-9]{3}$",
        message = "CVV must be 3 digits and cannot be 000"
    )
    private String cvv;

    @Pattern(
        regexp = "^[\\w.-]+@[\\w.-]+$",
        message = "Invalid UPI ID"
    )
    private String upiId;

    @Pattern(
        regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$",
        message = "Expiry must be in MM/YY format"
    )
    private String expiryDate;

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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}