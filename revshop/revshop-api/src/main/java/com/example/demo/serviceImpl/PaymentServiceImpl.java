package com.example.demo.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.Payment;
import com.example.demo.entity.PaymentMethod;
import com.example.demo.entity.PaymentStatus;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;   // 🔥 ADDED

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              CartRepository cartRepository) {   // 🔥 ADDED
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;   // 🔥 ADDED
    }

    @Override
    @Transactional
    public Payment processPayment(Long orderId,
                                  Double amount,
                                  PaymentMethod method) {

        if (orderId == null)
            throw new IllegalArgumentException("Order ID cannot be null");

        if (amount == null || amount <= 0)
            throw new IllegalArgumentException("Invalid payment amount");

        if (method == null)
            throw new IllegalArgumentException("Payment method must be selected");

        // 1️⃣ Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2️⃣ Simulate payment result (replace later with real gateway result)
        PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

        // 3️⃣ Create Payment record
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus(paymentStatus);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // 4️⃣ Handle order + stock + cart based on payment result
        if (paymentStatus == PaymentStatus.SUCCESS) {

            // ✅ Mark order as PAID
            order.setStatus(OrderStatus.PAID);

            // ✅ Reduce stock
            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();

                int updatedStock = product.getQuantity() - item.getQuantity();

                if (updatedStock < 0) {
                    throw new RuntimeException("Insufficient stock for product: "
                            + product.getName());
                }

                product.setQuantity(updatedStock);
            }

            // ✅ Clear Cart
            Cart cart = cartRepository.findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            cart.getItems().clear();   // orphanRemoval must be true

        } else {

            // ❌ Payment failed → Cancel order
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);

        return payment;
    }
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a paid order");
        }

        order.setStatus(OrderStatus.CANCELLED);
    }
}