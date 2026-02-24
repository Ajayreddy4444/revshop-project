
	package com.example.demo.controller;

	import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PaymentRequestDTO;
import com.example.demo.entity.Payment;
	import com.example.demo.service.PaymentService;

	@RestController
	@RequestMapping("/api/payments")
	public class PaymentController {

	    private final PaymentService paymentService;

	    public PaymentController(PaymentService paymentService) {
	        this.paymentService = paymentService;
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