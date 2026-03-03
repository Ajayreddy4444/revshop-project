package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);

        model.addAttribute("addresses",
                addressClientService.getAddressesByUser(userId));

        model.addAttribute("userId", userId);

        return "checkout";
    }

    // 🔹 Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam Long addressId,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        Long userId = getLoggedInUserId(session);
        if (userId == null) return "redirect:/login";

        try {

            PlaceOrderRequest request = new PlaceOrderRequest();
            request.setUserId(userId);
            request.setAddressId(addressId);

            OrderResponse response = orderClientService.placeOrder(request);

            return "redirect:/payment?orderId=" + response.getOrderId()
                    + "&amount=" + response.getTotalAmount();

        } catch (HttpClientErrorException ex) {

            // 🔥 Extract clean backend message only
            String backendMessage = ex.getResponseBodyAsString();

            if (backendMessage == null || backendMessage.isBlank()) {
                backendMessage = "Unable to place order. Please try again.";
            }

            redirectAttributes.addFlashAttribute("orderError", backendMessage);
            return "redirect:/orders/checkout";

        } catch (Exception ex) {

            redirectAttributes.addFlashAttribute("orderError",
                    "Something went wrong. Please try again.");

            return "redirect:/orders/checkout";
        }
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