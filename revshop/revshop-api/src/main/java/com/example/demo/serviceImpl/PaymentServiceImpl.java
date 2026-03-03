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
import com.example.demo.exception.InvalidPaymentRequestException;
import com.example.demo.exception.OrderAlreadyPaidException;
import com.example.demo.exception.OrderNotFoundException;
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
                                  PaymentMethod method,
                                  String cardNumber,
                                  String cvv,
                                  String upiId) {
    	System.out.println(" PAYMENT EXECUTED for order " + orderId);

        if (orderId == null)
            throw new InvalidPaymentRequestException("Order ID is required");

        if (amount == null || amount <= 0)
            throw new InvalidPaymentRequestException("Amount must be greater than 0");

        if (method == null)
            throw new InvalidPaymentRequestException("Payment method is required");

        validatePaymentDetails(method, cardNumber, cvv, upiId);

        //  Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        
      // Check already paid
        if (order.getStatus() == OrderStatus.PAID) {
            throw new OrderAlreadyPaidException("Payment already completed for this order");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidPaymentRequestException(
                "Cannot pay for a cancelled order"
            );
        }

        // Get amount ONLY from order (not from client)
        Double orderAmount = order.getTotalAmount();
        


        if (orderAmount == null || orderAmount <= 0) {
            throw new InvalidPaymentRequestException("Invalid order amount");
        }
        
        
        if (Double.compare(orderAmount, amount) != 0) {
            throw new InvalidPaymentRequestException(
                "Payment amount does not match order total"
            );
        }


        //  Simulate payment result
        PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

        //  Create Payment record
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(orderAmount);
        payment.setPaymentMethod(method);
        payment.setStatus(paymentStatus);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // Handle order + stock + cart based on payment result
        if (paymentStatus == PaymentStatus.SUCCESS) {

            //  Mark order as PAID
            order.setStatus(OrderStatus.PAID);

            // Reduce stock
            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();

                int updatedStock = product.getQuantity() - item.getQuantity();

//                if (updatedStock < 0) {
//                    throw new RuntimeException("Insufficient stock for product: "
//                            + product.getName());
//                }

                product.setQuantity(updatedStock);
            }

            //  Clear Cart
            Cart cart = cartRepository.findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            cart.getItems().clear();   

        } else {

            //  Payment failed → Cancel order
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);

        return payment;
    }

    private void validatePaymentDetails(PaymentMethod method,
                                        String cardNumber,
                                        String cvv,
                                        String upiId) {
        if (method == PaymentMethod.CARD) {
            if (cardNumber == null || !cardNumber.trim().matches("^\\d{16}$")) {
                throw new InvalidPaymentRequestException("Card number must be exactly 16 digits");
            }

            if (cvv == null || !cvv.trim().matches("^\\d{3}$")) {
                throw new InvalidPaymentRequestException("CVV must be exactly 3 digits");
            }
        }

        if (method == PaymentMethod.UPI) {
            if (upiId == null || !upiId.trim().matches("^[\\w.-]+@[\\w.-]+$")) {
                throw new InvalidPaymentRequestException("Invalid UPI ID");
            }
        }
    }
    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

    	 System.out.println(" CANCEL CALLED for orderId = " + orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a paid order");
        }

        order.setStatus(OrderStatus.CANCELLED);
    }
}
