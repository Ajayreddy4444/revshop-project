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
import com.example.demo.service.AddressClientService;

@Controller
@RequestMapping("/orders")
public class OrderpageController {

    private final CartClientService cartClientService;
    private final OrderClientService orderClientService;
    private final AddressClientService addressClientService;

    public OrderpageController(CartClientService cartClientService,
                               OrderClientService orderClientService,
                               AddressClientService addressClientService) {
        this.cartClientService = cartClientService;
        this.orderClientService = orderClientService;
        this.addressClientService = addressClientService;
    }

    // 🔹 Helper method to get logged-in user
    private Long getLoggedInUserId(HttpSession session) {
        AuthResponse user = (AuthResponse) session.getAttribute("user");
        return user != null ? user.getId() : null;
    }

    // 🔹 Checkout Page
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        List<CartResponse> cartItems = cartClientService.getCart(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);

        // 🔥 Fetch saved addresses
        model.addAttribute("addresses",
                addressClientService.getAddressesByUser(userId));

        model.addAttribute("userId", userId);

        return "checkout";
    }

    // 🔹 Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam Long addressId,
                             HttpSession session) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setUserId(userId);
        request.setAddressId(addressId);

        OrderResponse response = orderClientService.placeOrder(request);

        // 🔥 Redirect to payment page
        return "redirect:/payment?orderId=" + response.getOrderId()
                + "&amount=" + response.getTotalAmount();
    }

    // 🔹 My Orders
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



   