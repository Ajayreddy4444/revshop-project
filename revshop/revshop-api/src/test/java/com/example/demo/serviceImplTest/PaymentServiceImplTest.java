package com.example.demo.serviceImplTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import com.example.demo.service.NotificationService;
import com.example.demo.serviceImpl.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private CartRepository cartRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // VALIDATIONS 

    @Test
    void processPayment_nullOrderId() {

        assertThrows(InvalidPaymentRequestException.class,
                () -> paymentService.processPayment(
                        null,
                        1000.0,
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    @Test
    void processPayment_invalidAmount() {

        assertThrows(InvalidPaymentRequestException.class,
                () -> paymentService.processPayment(
                        1L,
                        0.0,
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    //  ORDER NOT FOUND

    @Test
    void processPayment_orderNotFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> paymentService.processPayment(
                        1L,
                        1000.0,
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    // ORDER ALREADY PAID 

    @Test
    void processPayment_orderAlreadyPaid() {

        Order order = new Order();
        order.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyPaidException.class,
                () -> paymentService.processPayment(
                        1L,
                        1000.0,
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    // CANCELLED ORDER 

    @Test
    void processPayment_cancelledOrder() {

        Order order = new Order();
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(InvalidPaymentRequestException.class,
                () -> paymentService.processPayment(
                        1L,
                        1000.0,
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    //  AMOUNT MISMATCH 

    @Test
    void processPayment_amountMismatch() {

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(5000.0);

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(InvalidPaymentRequestException.class,
                () -> paymentService.processPayment(
                        1L,
                        1000.0, // mismatch
                        PaymentMethod.CARD,
                        "1234567812345678",
                        "123",
                        null
                ));
    }

    @Test
    void processPayment_success() {

        Long orderId = 1L;

        Product product = new Product();
        product.setQuantity(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        User user = new User();
        user.setId(1L);

        Order order = new Order();
       
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(1000.0);
        order.setItems(List.of(item));
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
     // simulate DB-generated ID
        ReflectionTestUtils.setField(order, "id", 1L);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(cartRepository.findByUserId(1L))
                .thenReturn(Optional.of(cart));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Payment payment = paymentService.processPayment(
                orderId,
                1000.0,
                PaymentMethod.CARD,
                "1234567812345678",
                "123",
                null
        );

        assertNotNull(payment);
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(8, product.getQuantity());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(order);
    }
}  