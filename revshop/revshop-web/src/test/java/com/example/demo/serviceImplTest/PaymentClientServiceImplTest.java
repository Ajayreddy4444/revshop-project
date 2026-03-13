package com.example.demo.serviceImplTest;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.serviceImpl.PaymentClientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentClientServiceImpl paymentClientService;

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(paymentClientService, "baseUrl", BASE_URL);
    }

    // =========================
    // PROCESS PAYMENT
    // =========================

    @Test
    void processPayment_success() {

        PaymentRequest request = new PaymentRequest();

        paymentClientService.processPayment(request);

        verify(restTemplate).postForObject(
                BASE_URL + "/payments/process",
                request,
                Object.class);
    }

    @Test
    void processPayment_shouldThrowRuntimeException_withParsedErrorMessage() {

        PaymentRequest request = new PaymentRequest();

        String errorJson = "{\"error\":\"Insufficient balance\"}";

        HttpClientErrorException exception =
                new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        errorJson.getBytes(),
                        null
                );

        when(restTemplate.postForObject(
                BASE_URL + "/payments/process",
                request,
                Object.class))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> paymentClientService.processPayment(request));

        assertEquals("Insufficient balance", thrown.getMessage());
    }

    @Test
    void processPayment_shouldThrowGenericMessage_whenEmptyBody() {

        PaymentRequest request = new PaymentRequest();

        HttpClientErrorException exception =
                new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request"
                );

        when(restTemplate.postForObject(
                BASE_URL + "/payments/process",
                request,
                Object.class))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> paymentClientService.processPayment(request));

        assertEquals("Payment failed. Please try again.", thrown.getMessage());
    }

    // =========================
    // CANCEL ORDER
    // =========================

    @Test
    void cancelOrder_success() {

        paymentClientService.cancelOrder(1L);

        verify(restTemplate).postForObject(
                BASE_URL + "/payments/cancel/1",
                null,
                String.class);
    }

    @Test
    void cancelOrder_shouldThrowRuntimeException_onError() {

        String errorJson = "{\"error\":\"Order already processed\"}";

        HttpClientErrorException exception =
                new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST,
                        "Bad Request",
                        errorJson.getBytes(),
                        null
                );

        when(restTemplate.postForObject(
                BASE_URL + "/payments/cancel/1",
                null,
                String.class))
                .thenThrow(exception);

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> paymentClientService.cancelOrder(1L));

        assertEquals("Order already processed", thrown.getMessage());
    }
}
