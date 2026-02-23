package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CartResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.PlaceOrderRequest;
import com.example.demo.service.CartClientService;
import com.example.demo.service.OrderClientService;

@Controller
@RequestMapping("/orders")
public class OrderpageController {

    private final CartClientService cartClientService;
    private final OrderClientService orderClientService;

    public OrderpageController(CartClientService cartClientService,
                               OrderClientService orderClientService) {
        this.cartClientService = cartClientService;
        this.orderClientService = orderClientService;
    }


    @GetMapping("/checkout")
    public String showCheckoutPage(Model model) {

        Long userId = 1L; 

        List<CartResponse> cartItems =
                cartClientService.getCart(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("userId", userId);

        return "checkout";
    }

    // 🔥 Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam Long userId,
                             @RequestParam String paymentMethod,
                             @RequestParam String address,
                             Model model) {

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setUserId(userId);
        request.setPaymentMethod(paymentMethod);
        request.setAddress(address);

        OrderResponse response =
                orderClientService.placeOrder(request);

        model.addAttribute("order", response);

        return "success";
    }
    @GetMapping("/my-orders")
    public String viewOrders(Model model) {

        Long userId = 1L;

        List<OrderResponse> orders =
                orderClientService.getOrderByUser(userId);

        model.addAttribute("orders", orders);

        return "orders";
    }

}