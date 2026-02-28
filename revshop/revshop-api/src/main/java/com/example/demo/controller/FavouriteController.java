package com.example.demo.controller;

import com.example.demo.dto.FavouriteRequest;
import com.example.demo.entity.Favourite;
import com.example.demo.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;

    @PostMapping("/add")
    public String add(@RequestBody FavouriteRequest request) {

        favouriteService.addToFavourites(
                request.getUserId(),
                request.getProductId()
        );

        return "Added to favourites successfully";
    }

    @DeleteMapping("/remove")
    public String remove(@RequestParam Long userId,
                         @RequestParam Long productId) {

        favouriteService.removeFromFavourites(userId, productId);
        return "Removed from favourites successfully";
    }

    @GetMapping("/{userId}")
    public List<Favourite> getUserFavourites(@PathVariable Long userId) {

        return favouriteService.getUserFavourites(userId);
    }
}