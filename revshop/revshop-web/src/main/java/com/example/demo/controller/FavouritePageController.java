package com.example.demo.controller;

import com.example.demo.dto.FavouriteRequest;
import com.example.demo.service.FavouriteClientService;
import com.example.demo.service.ProductClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FavouritePageController {

    @Autowired
    private FavouriteClientService favouriteClientService;

    @Autowired
    private ProductClientService productClientService;

    // ===============================
    // ADD TO FAVOURITES
    // ===============================
    @PostMapping("/favourite/add")
    public String addToFavourite(@RequestParam Long productId,
                                 HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        // Safety check
        if (userId == null) {
            return "redirect:/login";
        }

        FavouriteRequest request = new FavouriteRequest();
        request.setUserId(userId);
        request.setProductId(productId);

        favouriteClientService.addToFavourites(request);

        // ⭐ Important for success message
        return "redirect:/products?favAdded=true";
    }

    // ===============================
    // VIEW FAVOURITES PAGE
    // ===============================
    @GetMapping("/favourites")
    public String viewFavourites(HttpSession session,
                                 Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Long> productIds =
                favouriteClientService.getUserFavouriteProductIds(userId);

        List<Object> favouriteProducts = new ArrayList<>();

        for (Long id : productIds) {
            Object product =
                    productClientService.getProductById(id);
            favouriteProducts.add(product);
        }

        model.addAttribute("favouriteProducts", favouriteProducts);

        return "favourites";
    }

    // ===============================
    // REMOVE FROM FAVOURITES
    // ===============================
    @PostMapping("/favourite/remove")
    public String removeFromFavourite(@RequestParam Long productId,
                                      HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        favouriteClientService.removeFromFavourites(userId, productId);

        return "redirect:/favourites";
    }
}