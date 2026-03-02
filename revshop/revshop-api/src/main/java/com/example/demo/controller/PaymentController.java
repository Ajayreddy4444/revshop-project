package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.entity.Payment;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;

	@RestController
	@RequestMapping("/api/payments")
	public class PaymentController {

	    private final PaymentService paymentService;
	     public PaymentController(PaymentService paymentService,
	    		OrderService orderService) {
	        this.paymentService = paymentService;
	    }
	    @PostMapping("/cancel/{orderId}")
	    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {

	        paymentService.cancelOrder(orderId);   // 👈 Same service method

	        return ResponseEntity.ok("Order cancelled successfully");
	    }
	    	
	@PostMapping("/process")
	public Payment processPayment(@RequestBody PaymentRequestDTO request) {

	    return paymentService.processPayment(
	            request.getOrderId(),
	            request.getAmount(),
	            request.getPaymentMethod()
	    );
	}
	}	