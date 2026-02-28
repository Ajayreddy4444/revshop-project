package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.service.OrderClientService;
import com.example.demo.service.PaymentClientService;

@Controller
@RequestMapping("/payment")
public class PaymentPageController {

	   private final PaymentClientService paymentClientService;
	   public PaymentPageController(PaymentClientService paymentClientService) {
	        this.paymentClientService = paymentClientService;
	        
	    }

    // 🔹 Show Payment Page
    @GetMapping
    public String showPaymentPage(@RequestParam Long orderId,
                                  @RequestParam Double amount,
                                  Model model) {

        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);

        return "payment";
    }

    // 🔹 Process Payment
    @PostMapping("/process")
    public String processPayment(@RequestParam Long orderId,
                                 @RequestParam Double amount,
                                 @RequestParam String paymentMethod,
                                 Model model) {

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(orderId);
        request.setAmount(amount);
        request.setPaymentMethod(paymentMethod);

        paymentClientService.processPayment(request);

        // Send orderId to success page
        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);
        model.addAttribute("paymentMethod", paymentMethod);

        return "success";
    }
    @PostMapping("/cancel")
    public String cancelOrder(@RequestParam Long orderId,
                              RedirectAttributes redirectAttributes) {

        //orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
    	  paymentClientService.cancelOrder(orderId);
        redirectAttributes.addFlashAttribute(
            "cancelMessage",
            "Order has been cancelled successfully!"
        );

        return "redirect:/orders/checkout";
    }
}