package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthResponse;
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

    // 🔹 Helper method to get logged-in user
    private Long getLoggedInUserId(HttpSession session) {
        AuthResponse user = (AuthResponse) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }

    // Checkout Page
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        List<CartResponse> cartItems =
                cartClientService.getCart(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);
        return "checkout";
    }

    // Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String paymentMethod,
                             @RequestParam String address,
                             Model model,
                             HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setUserId(userId);
        request.setPaymentMethod(paymentMethod);
        request.setAddress(address);

        OrderResponse response =
                orderClientService.placeOrder(request);

        model.addAttribute("order", response);

        return "success";
    }

    // My Orders
    @GetMapping("/my-orders")
    public String viewOrders(Model model, HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        List<OrderResponse> orders =
                orderClientService.getOrderByUser(userId);

        model.addAttribute("orders", orders);

        return "orders";
    }
}



//package com.example.demo.controller;
//
//import java.util.List;
//
//import jakarta.servlet.http.HttpSession;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.demo.dto.AuthResponse;
//import com.example.demo.dto.CartResponse;
//import com.example.demo.dto.OrderResponse;
//import com.example.demo.dto.PlaceOrderRequest;
//import com.example.demo.service.CartClientService;
//import com.example.demo.service.OrderClientService;
//
//@Controller
//@RequestMapping("/orders")
//public class OrderpageController {
//
//    private final CartClientService cartClientService;
//    private final OrderClientService orderClientService;
//
//    public OrderpageController(CartClientService cartClientService,
//                               OrderClientService orderClientService) {
//        this.cartClientService = cartClientService;
//        this.orderClientService = orderClientService;
//    }
//
//    // 🔹 Helper method to get logged-in user ID from session
//    private Long getLoggedInUserId(HttpSession session) {
//
//        AuthResponse user =
//                (AuthResponse) session.getAttribute("user");
//
//        if (user == null) {
//            return null;
//        }
//
//        return user.getId();   // 🔥 Correct field from AuthResponse
//    }
//
//    // 🔹 Checkout Page
//    @GetMapping("/checkout")
//    public String showCheckoutPage(Model model, HttpSession session) {
//
//        Long userId = getLoggedInUserId(session);
//        if (userId == null) return "redirect:/login";
//
//        List<CartResponse> cartItems =
//                cartClientService.getCart(userId);
//
//        if (cartItems == null || cartItems.isEmpty()) {
//            return "redirect:/cart";
//        }
//
//        model.addAttribute("cartItems", cartItems);
//        return "checkout";
//    }
//
//    // 🔹 Place Order
//    @PostMapping("/place")
//    public String placeOrder(@RequestParam String paymentMethod,
//                             @RequestParam String address,
//                             Model model,
//                             HttpSession session) {
//
//        Long userId = getLoggedInUserId(session);
//        if (userId == null) return "redirect:/login";
//
//        PlaceOrderRequest request = new PlaceOrderRequest();
//        request.setUserId(userId);
//        request.setPaymentMethod(paymentMethod);
//        request.setAddress(address);
//
//        OrderResponse response =
//                orderClientService.placeOrder(request);
//
//        model.addAttribute("order", response);
//
//        return "success";
//    }
//
//    // 🔹 My Orders
//    @GetMapping("/my-orders")
//    public String viewOrders(Model model, HttpSession session) {
//
//        Long userId = getLoggedInUserId(session);
//        if (userId == null) return "redirect:/login";
//
//        List<OrderResponse> orders =
//                orderClientService.getOrderByUser(userId);
//
//        model.addAttribute("orders", orders);
//
//        return "orders";
//    }
//}
//   