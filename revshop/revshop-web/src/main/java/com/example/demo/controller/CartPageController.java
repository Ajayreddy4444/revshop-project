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

        if(user == null){
            return "redirect:/login";
        }

        model.addAttribute("cart",
                cartService.getCart(user.getId()));

        return "cart";
    }

    // ⭐ UPDATED ADD METHOD (SMART REDIRECT)
    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                      @RequestParam int quantity,
                      @RequestHeader(value="referer", required=false) String referer,
                      HttpSession session){

        AuthResponse user = (AuthResponse) session.getAttribute("user");

        if(user == null){
            return "redirect:/login";
        }

        CartRequest req = new CartRequest();
        req.setUserId(user.getId());
        req.setProductId(productId);
        req.setQuantity(quantity);

        cartService.add(req);

        // ⭐ If user came from product-details page
        if(referer != null && referer.contains("/products/")){
            return "redirect:" + referer + "?added=true";
        }

        // ⭐ Otherwise products page
        return "redirect:/products?added=true&pid="+productId;
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id,
                         HttpSession session){

        AuthResponse user = (AuthResponse) session.getAttribute("user");

        if(user == null){
            return "redirect:/login";
        }

        cartService.remove(id);

        return "redirect:/cart";
    }
}