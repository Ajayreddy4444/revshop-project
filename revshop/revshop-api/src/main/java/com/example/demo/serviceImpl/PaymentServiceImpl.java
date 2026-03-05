package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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
import com.example.demo.service.NotificationService;
import com.example.demo.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final NotificationService notificationService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              CartRepository cartRepository,
                              NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Payment processPayment(Long orderId,
                                  Double amount,
                                  PaymentMethod method,
                                  String cardNumber,
                                  String cvv,
                                  String upiId,
                                  String expiryDate) {

        if (orderId == null)
            throw new InvalidPaymentRequestException("Order ID is required");

        if (amount == null || amount <= 0)
            throw new InvalidPaymentRequestException("Amount must be greater than 0");

        if (method == null)
            throw new InvalidPaymentRequestException("Payment method is required");

        validatePaymentDetails(method, cardNumber, cvv, upiId ,expiryDate);

        // Fetch Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID)
            throw new OrderAlreadyPaidException("Payment already completed for this order");

        if (order.getStatus() == OrderStatus.CANCELLED)
            throw new InvalidPaymentRequestException("Cannot pay for a cancelled order");

        Double orderAmount = order.getTotalAmount();

        if (orderAmount == null || orderAmount <= 0)
            throw new InvalidPaymentRequestException("Invalid order amount");

        if (Double.compare(orderAmount, amount) != 0)
            throw new InvalidPaymentRequestException("Payment amount does not match order total");

        // Simulate payment success
        PaymentStatus paymentStatus = PaymentStatus.SUCCESS;

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(orderAmount);
        payment.setPaymentMethod(method);
        payment.setStatus(paymentStatus);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        if (paymentStatus == PaymentStatus.SUCCESS) {

            order.setStatus(OrderStatus.PAID);

            // Reduce stock + Low stock alert
            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();
                int updatedStock = product.getQuantity() - item.getQuantity();

                if (updatedStock < 0) {
                    throw new InvalidPaymentRequestException(
                            "Insufficient stock for product: " + product.getName());
                }

                product.setQuantity(updatedStock);

                if (product.getLowStockThreshold() != null &&
                        updatedStock <= product.getLowStockThreshold() &&
                        product.getSeller() != null) {

                    String warningMessage =
                            "Product: " + product.getName() +
                            "\nRemaining Qty: " + updatedStock;

                    notificationService.createLowStockNotification(
                            product.getSeller(),
                            warningMessage);
                }
            }

            // Clear Cart
            Cart cart = cartRepository.findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new InvalidPaymentRequestException("Cart not found"));

            cart.getItems().clear();

            // Format date
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

            String formattedDate =
                    order.getOrderDate().format(formatter);

            
            // Collect product names
            String productNames =
                    order.getItems()
                            .stream()
                            .map(item -> item.getProduct().getName())
                            .collect(Collectors.joining(", "));

            // Buyer notification
            String buyerMessage =
                    "Your order has been placed successfully.\n"
                    		 + "Product: " + productNames + "\n"
                             + "Order ID: " + order.getId()
                             + " at " + formattedDate;

            notificationService.createOrderNotification(
                    order.getUser(),
                    order.getId(),
                    buyerMessage);

            // Seller notification
            order.getItems().forEach(item -> {

                if (item.getProduct().getSeller() != null) {

                    String sellerMessage =
                    		 "Product: "
                                     + item.getProduct().getName()
                                     + "\nOrder ID: "
                            + order.getId()
                            + " | "
                            + formattedDate;

                    notificationService.createSellerOrderNotification(
                            item.getProduct().getSeller(),
                            order.getId(),
                            sellerMessage
                    );
                }
            });

        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);

        return payment;
    }

    private void validatePaymentDetails(PaymentMethod method,
            String cardNumber,
            String cvv,
            String upiId,
            String expiryDate) {

                      if (method == PaymentMethod.CARD) {

                      if (cardNumber == null ||
                                  !cardNumber.trim().matches("^(?!0+$)(?!([0-9])\\1+$)[0-9]{16}$")) {

                                    throw new InvalidPaymentRequestException("Invalid card number");
}

                   if (cvv == null ||
                              !cvv.trim().matches("^(?!000$)[0-9]{3}$")) {

                                throw new InvalidPaymentRequestException("Invalid CVV");
}

                          if (expiryDate == null ||
                                     !expiryDate.trim().matches("^(0[1-9]|1[0-2])\\/([0-9]{2})$")) {

                          throw new InvalidPaymentRequestException("Expiry must be MM/YY");
}
}

                             if (method == PaymentMethod.UPI) {

                                  if (upiId == null ||
                        !upiId.trim().matches("^[\\w.-]+@[\\w.-]+$")) {

                      throw new InvalidPaymentRequestException("Invalid UPI ID");
}
}
}

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.PAID)
            throw new InvalidPaymentRequestException("Cannot cancel a paid order");

        order.setStatus(OrderStatus.CANCELLED);
    }
} 