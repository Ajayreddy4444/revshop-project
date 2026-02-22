package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.CartRequest;
import com.example.demo.service.CartClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartPageController {

    private final CartClientService cartService;

    public CartPageController(CartClientService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model){

        AuthResponse user = (AuthResponse) session.getAttribute("user");

        if(user == null) return "redirect:/login";

        model.addAttribute("cart",
                cartService.getCart(user.getId()));

        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                      @RequestParam int quantity,
                      HttpSession session){

        AuthResponse user = (AuthResponse) session.getAttribute("user");

        if(user == null) return "redirect:/login";

        CartRequest req = new CartRequest();
        req.setUserId(user.getId());
        req.setProductId(productId);
        req.setQuantity(quantity);

        cartService.add(req);

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id){
        cartService.remove(id);
        return "redirect:/cart";
    }
}